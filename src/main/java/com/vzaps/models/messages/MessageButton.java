package com.vzaps.models.messages;

import com.vzaps.models.common.VZapsModel;

public final class MessageButton extends VZapsModel {
  private final String id;
  private final String text;

  private MessageButton(Builder builder) {
    this.id = builder.id;
    this.text = builder.text;
  }

  public static Builder builder() {
    return new Builder();
  }

  public String id() {
    return id;
  }

  public String text() {
    return text;
  }

  public static final class Builder {
    private String id;
    private String text;

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder text(String text) {
      this.text = text;
      return this;
    }

    public MessageButton build() {
      return new MessageButton(this);
    }
  }
}
