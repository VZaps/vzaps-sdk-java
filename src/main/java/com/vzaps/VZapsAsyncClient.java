package com.vzaps;

import com.fasterxml.jackson.databind.JsonNode;
import com.vzaps.models.common.InstanceListRequest;
import com.vzaps.models.messages.SendTextMessageRequest;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

public final class VZapsAsyncClient implements AutoCloseable {
  private final VZapsClient sync;
  private final Executor executor;
  private final InstancesAsync instances;
  private final MessagesAsync messages;

  public VZapsAsyncClient(VZapsClientOptions options) {
    this(options, ForkJoinPool.commonPool());
  }

  public VZapsAsyncClient(VZapsClientOptions options, Executor executor) {
    this.sync = new VZapsClient(options);
    this.executor = executor == null ? ForkJoinPool.commonPool() : executor;
    this.instances = new InstancesAsync();
    this.messages = new MessagesAsync();
  }

  public static VZapsAsyncClientBuilder builder() {
    return new VZapsAsyncClientBuilder();
  }

  public static VZapsAsyncClientBuilder asyncBuilder() {
    return new VZapsAsyncClientBuilder();
  }

  public CompletableFuture<String> getAccessToken() {
    return supply(() -> sync.auth().getAccessToken());
  }

  public InstancesAsync instances() {
    return instances;
  }

  public MessagesAsync messages() {
    return messages;
  }

  public <T> CompletableFuture<T> request(
      String method, String path, Object body, VZapsRequestOptions options, Class<T> type) {
    return supply(() -> sync.request(method, path, body, options, type));
  }

  @Override
  public void close() {
    sync.close();
  }

  private <T> CompletableFuture<T> supply(java.util.function.Supplier<T> supplier) {
    return CompletableFuture.supplyAsync(supplier, executor);
  }

  public final class InstancesAsync {
    public CompletableFuture<JsonNode> list() {
      return supply(() -> sync.instances().list());
    }

    public CompletableFuture<JsonNode> list(InstanceListRequest request) {
      return supply(() -> sync.instances().list(request));
    }
  }

  public final class MessagesAsync {
    public CompletableFuture<JsonNode> sendText(SendTextMessageRequest request) {
      return supply(() -> sync.messages().sendText(request));
    }
  }
}
