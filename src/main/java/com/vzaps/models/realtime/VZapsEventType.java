package com.vzaps.models.realtime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum VZapsEventType {
  ALL("all"),
  MESSAGE("message"),
  CONNECTED("connected"),
  DISCONNECTED("disconnected"),
  QR("qr"),
  STATUS("status"),
  WEBHOOK("webhook"),
  UNKNOWN("unknown");

  private final String value;

  VZapsEventType(String value) {
    this.value = value;
  }

  @JsonValue
  public String value() {
    return value;
  }

  @JsonCreator
  public static VZapsEventType fromValue(String value) {
    if (value == null) {
      return UNKNOWN;
    }
    for (VZapsEventType type : values()) {
      if (type.value.equalsIgnoreCase(value)) {
        return type;
      }
    }
    return UNKNOWN;
  }
}
