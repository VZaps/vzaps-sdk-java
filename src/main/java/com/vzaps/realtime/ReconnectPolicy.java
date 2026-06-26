package com.vzaps.realtime;

import java.time.Duration;

public final class ReconnectPolicy {
  public static final ReconnectPolicy DEFAULT =
      ReconnectPolicy.builder()
          .initialDelay(Duration.ofMillis(500))
          .maxDelay(Duration.ofSeconds(15))
          .build();

  private final Duration initialDelay;
  private final Duration maxDelay;

  private ReconnectPolicy(Builder builder) {
    this.initialDelay = builder.initialDelay;
    this.maxDelay = builder.maxDelay;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Duration initialDelay() {
    return initialDelay;
  }

  public Duration maxDelay() {
    return maxDelay;
  }

  public static final class Builder {
    private Duration initialDelay = Duration.ofMillis(500);
    private Duration maxDelay = Duration.ofSeconds(15);

    public Builder initialDelay(Duration initialDelay) {
      this.initialDelay = initialDelay;
      return this;
    }

    public Builder maxDelay(Duration maxDelay) {
      this.maxDelay = maxDelay;
      return this;
    }

    public ReconnectPolicy build() {
      return new ReconnectPolicy(this);
    }
  }
}
