package com.vzaps.exceptions;

/** Public API error returned by VZaps. */
public class VZapsApiException extends VZapsException {
  private final int statusCode;
  private final String errorCode;
  private final String details;
  private final String requestId;
  private final String responseBody;

  public VZapsApiException(
      String message,
      int statusCode,
      String errorCode,
      String details,
      String requestId,
      String responseBody) {
    super(message);
    this.statusCode = statusCode;
    this.errorCode = errorCode;
    this.details = details;
    this.requestId = requestId;
    this.responseBody = responseBody;
  }

  public int statusCode() {
    return statusCode;
  }

  public String errorCode() {
    return errorCode;
  }

  public String details() {
    return details;
  }

  public String requestId() {
    return requestId;
  }

  public String responseBody() {
    return responseBody;
  }
}
