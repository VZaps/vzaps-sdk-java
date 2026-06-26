package com.vzaps.exceptions;

public class VZapsRealtimeException extends VZapsException {
  public VZapsRealtimeException(String message) {
    super(message);
  }

  public VZapsRealtimeException(String message, Throwable cause) {
    super(message, cause);
  }
}
