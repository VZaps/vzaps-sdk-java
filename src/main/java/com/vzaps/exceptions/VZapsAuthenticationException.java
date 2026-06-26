package com.vzaps.exceptions;

public class VZapsAuthenticationException extends VZapsApiException {
  public VZapsAuthenticationException(
      String message,
      int statusCode,
      String errorCode,
      String details,
      String requestId,
      String responseBody) {
    super(message, statusCode, errorCode, details, requestId, responseBody);
  }
}
