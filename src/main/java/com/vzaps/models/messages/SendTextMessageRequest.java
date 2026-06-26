package com.vzaps.models.messages;

import com.vzaps.models.common.InstanceScopedRequest;

public final class SendTextMessageRequest extends InstanceScopedRequest {
  private final String phone;
  private final String message;

  private SendTextMessageRequest(Builder builder) {
    super(builder);
    this.phone = requireText(builder.phone, "phone");
    this.message = requireText(builder.message, "message");
  }

  public static Builder builder() {
    return new Builder();
  }

  public String phone() {
    return phone;
  }

  public String message() {
    return message;
  }

  public static final class Builder extends InstanceScopedRequest.AbstractBuilder<Builder> {
    private String phone;
    private String message;

    public Builder phone(String phone) {
      this.phone = phone;
      return this;
    }

    public Builder message(String message) {
      this.message = message;
      return this;
    }

    @Override
    protected Builder self() {
      return this;
    }

    public SendTextMessageRequest build() {
      return new SendTextMessageRequest(this);
    }
  }
}
