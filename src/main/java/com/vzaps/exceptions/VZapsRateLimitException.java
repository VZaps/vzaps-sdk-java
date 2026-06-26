package com.vzaps.exceptions;

public class VZapsRateLimitException extends VZapsApiException {
  public VZapsRateLimitException(
      String message,
      int statusCode,
      String errorCode,
      String details,
      String requestId,
      String responseBody) {
    super(message, statusCode, errorCode, details, requestId, responseBody);
  }
}
