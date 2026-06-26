package com.vzaps.exceptions;

/** Base unchecked exception for SDK failures. */
public class VZapsException extends RuntimeException {
  public VZapsException(String message) {
    super(message);
  }

  public VZapsException(String message, Throwable cause) {
    super(message, cause);
  }
}
