package com.vzaps.http;

import com.vzaps.VZapsRequestOptions;

/** Low-level request for escape-hatch integrations. */
public final class VZapsHttpRequest {
  private final String method;
  private final String path;
  private final Object body;
  private final VZapsRequestOptions options;

  private VZapsHttpRequest(Builder builder) {
    this.method = builder.method;
    this.path = builder.path;
    this.body = builder.body;
    this.options = builder.options == null ? VZapsRequestOptions.defaults() : builder.options;
  }

  public static Builder builder() {
    return new Builder();
  }

  public String method() {
    return method;
  }

  public String path() {
    return path;
  }

  public Object body() {
    return body;
  }

  public VZapsRequestOptions options() {
    return options;
  }

  public static final class Builder {
    private String method = "GET";
    private String path;
    private Object body;
    private VZapsRequestOptions options;

    public Builder method(String method) {
      this.method = method;
      return this;
    }

    public Builder path(String path) {
      this.path = path;
      return this;
    }

    public Builder body(Object body) {
      this.body = body;
      return this;
    }

    public Builder options(VZapsRequestOptions options) {
      this.options = options;
      return this;
    }

    public VZapsHttpRequest build() {
      if (path == null || path.trim().isEmpty()) {
        throw new IllegalArgumentException("path is required.");
      }
      return new VZapsHttpRequest(this);
    }
  }
}
