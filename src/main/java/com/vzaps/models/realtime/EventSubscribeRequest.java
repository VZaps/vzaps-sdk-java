package com.vzaps.models.realtime;

import com.vzaps.models.common.InstanceScopedRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class EventSubscribeRequest extends InstanceScopedRequest {
  private final List<VZapsEventType> events;
  private final boolean reconnect;
  private final String lastEventId;

  private EventSubscribeRequest(Builder builder) {
    super(builder);
    this.events = Collections.unmodifiableList(new ArrayList<>(builder.events));
    this.reconnect = builder.reconnect;
    this.lastEventId = builder.lastEventId;
  }

  public static Builder builder() {
    return new Builder();
  }

  public List<VZapsEventType> events() {
    return events;
  }

  public boolean reconnect() {
    return reconnect;
  }

  public String lastEventId() {
    return lastEventId;
  }

  public static final class Builder extends InstanceScopedRequest.AbstractBuilder<Builder> {
    private final List<VZapsEventType> events = new ArrayList<>();
    private boolean reconnect = true;
    private String lastEventId;

    public Builder event(VZapsEventType event) {
      if (event != null) {
        events.add(event);
      }
      return this;
    }

    public Builder events(List<VZapsEventType> events) {
      if (events != null) {
        events.forEach(this::event);
      }
      return this;
    }

    public Builder reconnect(boolean reconnect) {
      this.reconnect = reconnect;
      return this;
    }

    public Builder lastEventId(String lastEventId) {
      this.lastEventId = lastEventId;
      return this;
    }

    @Override
    protected Builder self() {
      return this;
    }

    public EventSubscribeRequest build() {
      return new EventSubscribeRequest(this);
    }
  }
}
