package com.vzaps.models.common;

/** Flexible instance-scoped request for endpoints whose payloads are intentionally dynamic. */
public final class GenericInstanceRequest extends InstanceScopedRequest {
  private GenericInstanceRequest(Builder builder) {
    super(builder);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder extends InstanceScopedRequest.AbstractBuilder<Builder> {
    @Override
    protected Builder self() {
      return this;
    }

    public GenericInstanceRequest build() {
      return new GenericInstanceRequest(this);
    }
  }
}
