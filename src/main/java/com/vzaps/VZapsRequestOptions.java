package com.vzaps;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** Per-request knobs for raw and typed SDK calls. */
public final class VZapsRequestOptions {
  private final boolean authenticate;
  private final String instanceToken;
  private final Map<String, Object> query;
  private final Map<String, String> headers;

  private VZapsRequestOptions(Builder builder) {
    this.authenticate = builder.authenticate;
    this.instanceToken = builder.instanceToken;
    this.query = Collections.unmodifiableMap(new LinkedHashMap<>(builder.query));
    this.headers = Collections.unmodifiableMap(new LinkedHashMap<>(builder.headers));
  }

  public static Builder builder() {
    return new Builder();
  }

  public static VZapsRequestOptions defaults() {
    return builder().build();
  }

  public boolean authenticate() {
    return authenticate;
  }

  public String instanceToken() {
    return instanceToken;
  }

  public Map<String, Object> query() {
    return query;
  }

  public Map<String, String> headers() {
    return headers;
  }

  public static final class Builder {
    private boolean authenticate = true;
    private String instanceToken;
    private final Map<String, Object> query = new LinkedHashMap<>();
    private final Map<String, String> headers = new LinkedHashMap<>();

    public Builder authenticate(boolean authenticate) {
      this.authenticate = authenticate;
      return this;
    }

    public Builder instanceToken(String instanceToken) {
      this.instanceToken = instanceToken;
      return this;
    }

    public Builder query(String key, Object value) {
      if (value != null) {
        query.put(key, value);
      }
      return this;
    }

    public Builder query(Map<String, ?> values) {
      if (values != null) {
        values.forEach((key, value) -> query(key, value));
      }
      return this;
    }

    public Builder header(String key, String value) {
      if (value != null) {
        headers.put(key, value);
      }
      return this;
    }

    public Builder headers(Map<String, String> values) {
      if (values != null) {
        values.forEach(this::header);
      }
      return this;
    }

    public VZapsRequestOptions build() {
      return new VZapsRequestOptions(this);
    }
  }
}
