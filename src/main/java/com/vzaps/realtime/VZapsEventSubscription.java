package com.vzaps.realtime;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vzaps.VZapsClientOptions;
import com.vzaps.exceptions.VZapsRealtimeException;
import com.vzaps.http.VZapsHttpClient;
import com.vzaps.json.VZapsJson;
import com.vzaps.models.realtime.EventSubscribeRequest;
import com.vzaps.models.realtime.VZapsEvent;
import com.vzaps.models.realtime.VZapsEventType;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.WebSocket;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/** Active realtime WebSocket subscription. Delivery is at-least-once; dedupe by event id. */
public final class VZapsEventSubscription implements AutoCloseable {
  private final VZapsHttpClient http;
  private final VZapsClientOptions options;
  private final EventSubscribeRequest request;
  private final ReconnectPolicy reconnectPolicy;
  private final CountDownLatch closed = new CountDownLatch(1);
  private final Map<VZapsEventType, List<Consumer<VZapsEvent>>> handlers =
      new ConcurrentHashMap<>();
  private volatile WebSocket socket;
  private volatile boolean closing;
  private volatile String lastEventId;

  public VZapsEventSubscription(
      VZapsHttpClient http,
      VZapsClientOptions options,
      EventSubscribeRequest request,
      ReconnectPolicy reconnectPolicy) {
    this.http = http;
    this.options = options;
    this.request = request;
    this.reconnectPolicy = reconnectPolicy == null ? ReconnectPolicy.DEFAULT : reconnectPolicy;
    this.lastEventId = request.lastEventId();
    connect();
  }

  public VZapsEventSubscription on(VZapsEventType type, Consumer<VZapsEvent> handler) {
    handlers.computeIfAbsent(type, ignored -> new CopyOnWriteArrayList<>()).add(handler);
    return this;
  }

  public void awaitClose() throws InterruptedException {
    closed.await();
  }

  public boolean awaitClose(Duration timeout) throws InterruptedException {
    return closed.await(timeout.toMillis(), TimeUnit.MILLISECONDS);
  }

  @Override
  public void close() {
    closing = true;
    WebSocket current = socket;
    if (current != null) {
      current.sendClose(WebSocket.NORMAL_CLOSURE, "client closing");
    }
    closed.countDown();
  }

  private void connect() {
    URI uri = buildUri();
    java.net.http.WebSocket.Builder builder =
        http.innerClient()
            .newWebSocketBuilder()
            .header("Authorization", "Bearer " + http.getAccessToken())
            .header("X-Client-Token", options.clientToken());
    if (request.instanceToken() != null && !request.instanceToken().trim().isEmpty()) {
      builder.header("X-Instance-Token", request.instanceToken());
    }
    socket = builder.buildAsync(uri, new Listener()).join();
  }

  private URI buildUri() {
    StringBuilder query = new StringBuilder();
    query.append("instance_id=").append(encode(request.instanceId()));
    if (lastEventId != null && !lastEventId.trim().isEmpty()) {
      query.append("&last_event_id=").append(encode(lastEventId));
    }
    if (!request.events().isEmpty()) {
      for (VZapsEventType type : request.events()) {
        query.append("&events=").append(encode(type.value()));
      }
    }
    String base = options.realtimeUrl().toString();
    if (base.endsWith("/")) {
      base = base.substring(0, base.length() - 1);
    }
    return URI.create(base + "/events/ws?" + query);
  }

  private void dispatch(String message) {
    VZapsEvent event = VZapsJson.read(message, VZapsEvent.class);
    if (event.id() != null) {
      lastEventId = event.id();
    }
    runHandlers(VZapsEventType.ALL, event);
    runHandlers(event.type(), event);
    ack(event);
  }

  private void runHandlers(VZapsEventType type, VZapsEvent event) {
    List<Consumer<VZapsEvent>> current = handlers.get(type);
    if (current == null) {
      return;
    }
    for (Consumer<VZapsEvent> handler : current) {
      handler.accept(event);
    }
  }

  private void ack(VZapsEvent event) {
    WebSocket current = socket;
    if (current == null || event.id() == null) {
      return;
    }
    ObjectNode ack = VZapsJson.objectNode();
    ack.put("type", "ack");
    ack.put("id", event.id());
    current.sendText(ack.toString(), true);
  }

  private void reconnect() {
    if (closing || !request.reconnect()) {
      closed.countDown();
      return;
    }
    try {
      Thread.sleep(reconnectPolicy.initialDelay().toMillis());
      connect();
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
      closed.countDown();
    }
  }

  private static String encode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
  }

  private final class Listener implements WebSocket.Listener {
    private final StringBuilder partial = new StringBuilder();

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
      partial.append(data);
      if (last) {
        String message = partial.toString();
        partial.setLength(0);
        try {
          dispatch(message);
        } catch (RuntimeException ex) {
          throw new VZapsRealtimeException("Failed to handle VZaps realtime event.", ex);
        }
      }
      webSocket.request(1);
      return null;
    }

    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
      if (closing) {
        closed.countDown();
      } else {
        reconnect();
      }
      return null;
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error) {
      if (!closing) {
        reconnect();
      } else {
        closed.countDown();
      }
    }
  }
}
