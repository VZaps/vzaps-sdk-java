package com.vzaps.examples;

import com.vzaps.VZapsClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VZapsConfiguration {
  @Bean
  VZapsClient vzapsClient() {
    return VZapsClient.builder()
        .clientToken(System.getenv("VZAPS_CLIENT_TOKEN"))
        .clientSecret(System.getenv("VZAPS_CLIENT_SECRET"))
        .build();
  }
}
