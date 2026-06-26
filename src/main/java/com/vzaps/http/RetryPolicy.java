package com.vzaps.http;

import java.time.Duration;

/** Conservative retry policy. Disabled by default for mutating calls. */
public final class RetryPolicy {
  public static final RetryPolicy NONE = new RetryPolicy(0, Duration.ofMillis(200), false);

  private final int maxAttempts;
  private final Duration baseDelay;
  private final boolean retryMutations;

  private RetryPolicy(int maxAttempts, Duration baseDelay, boolean retryMutations) {
    this.maxAttempts = maxAttempts;
    this.baseDelay = baseDelay;
    this.retryMutations = retryMutations;
  }

  public static RetryPolicy of(int maxAttempts, Duration baseDelay, boolean retryMutations) {
    if (maxAttempts < 0) {
      throw new IllegalArgumentException("maxAttempts must not be negative.");
    }
    return new RetryPolicy(maxAttempts, baseDelay, retryMutations);
  }

  public int maxAttempts() {
    return maxAttempts;
  }

  public Duration baseDelay() {
    return baseDelay;
  }

  public boolean retryMutations() {
    return retryMutations;
  }
}
