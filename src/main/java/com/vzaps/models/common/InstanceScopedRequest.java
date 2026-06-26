package com.vzaps.models.common;

/** Base request carrying the instance id and optional X-Instance-Token header value. */
public abstract class InstanceScopedRequest extends VZapsModel {
  private final String instanceId;
  private final String instanceToken;

  protected InstanceScopedRequest(AbstractBuilder<?> builder) {
    this.instanceId = requireText(builder.instanceId, "instanceId");
    this.instanceToken = builder.instanceToken;
    putAllAdditionalData(builder.additionalData);
  }

  public String instanceId() {
    return instanceId;
  }

  public String instanceToken() {
    return instanceToken;
  }

  protected static String requireText(String value, String name) {
    if (value == null || value.trim().isEmpty()) {
      throw new IllegalArgumentException(name + " is required.");
    }
    return value;
  }

  public abstract static class AbstractBuilder<T extends AbstractBuilder<T>> {
    private String instanceId;
    private String instanceToken;
    private final java.util.Map<String, Object> additionalData = new java.util.LinkedHashMap<>();

    public T instanceId(String instanceId) {
      this.instanceId = instanceId;
      return self();
    }

    public T instanceToken(String instanceToken) {
      this.instanceToken = instanceToken;
      return self();
    }

    public T put(String key, Object value) {
      if (value != null) {
        additionalData.put(key, value);
      }
      return self();
    }

    public T putAll(java.util.Map<String, ?> values) {
      if (values != null) {
        values.forEach(this::put);
      }
      return self();
    }

    protected abstract T self();
  }
}
