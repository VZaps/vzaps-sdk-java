package com.vzaps.models.common;

/** Options for instance endpoints where the instance id is already in the method arguments. */
public final class InstanceRequestOptions {
  private final String instanceToken;

  private InstanceRequestOptions(Builder builder) {
    this.instanceToken = builder.instanceToken;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static InstanceRequestOptions none() {
    return builder().build();
  }

  public String instanceToken() {
    return instanceToken;
  }

  public static final class Builder {
    private String instanceToken;

    public Builder instanceToken(String instanceToken) {
      this.instanceToken = instanceToken;
      return this;
    }

    public InstanceRequestOptions build() {
      return new InstanceRequestOptions(this);
    }
  }
}
