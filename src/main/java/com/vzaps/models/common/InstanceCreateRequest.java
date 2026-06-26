package com.vzaps.models.common;

public final class InstanceCreateRequest extends VZapsModel {
  private final String name;
  private final String webhook;
  private final Object eventsSubscribe;

  private InstanceCreateRequest(Builder builder) {
    this.name = builder.name;
    this.webhook = builder.webhook;
    this.eventsSubscribe = builder.eventsSubscribe;
    putAllAdditionalData(builder.additionalData);
  }

  public static Builder builder() {
    return new Builder();
  }

  public String name() {
    return name;
  }

  public String webhook() {
    return webhook;
  }

  public Object eventsSubscribe() {
    return eventsSubscribe;
  }

  public static final class Builder {
    private String name;
    private String webhook;
    private Object eventsSubscribe;
    private final java.util.Map<String, Object> additionalData = new java.util.LinkedHashMap<>();

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder webhook(String webhook) {
      this.webhook = webhook;
      return this;
    }

    public Builder eventsSubscribe(Object eventsSubscribe) {
      this.eventsSubscribe = eventsSubscribe;
      return this;
    }

    public Builder put(String key, Object value) {
      if (value != null) {
        additionalData.put(key, value);
      }
      return this;
    }

    public InstanceCreateRequest build() {
      return new InstanceCreateRequest(this);
    }
  }
}
