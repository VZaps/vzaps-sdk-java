package com.vzaps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.databind.JsonNode;
import com.vzaps.exceptions.VZapsRateLimitException;
import com.vzaps.models.common.InstanceRequestOptions;
import com.vzaps.models.messages.SendTextMessageRequest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.Test;

final class VZapsClientTest {
  @Test
  void validatesRequiredCredentials() {
    assertThatThrownBy(() -> VZapsClient.builder().clientToken("token").build())
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("clientSecret");
  }

  @Test
  void sendsTextMessageWithCachedTokenAndInstanceHeader() throws Exception {
    try (MockWebServer server = new MockWebServer()) {
      server.enqueue(json("{\"accessToken\":\"jwt-token\",\"expiresIn\":3600}"));
      server.enqueue(json("{\"ok\":true}"));
      server.enqueue(json("{\"ok\":true}"));

      try (VZapsClient client = client(server)) {
        SendTextMessageRequest request =
            SendTextMessageRequest.builder()
                .instanceId("VZ123")
                .instanceToken("instance-token")
                .phone("5511999999999")
                .message("Hello from Java")
                .build();

        JsonNode first = client.messages().sendText(request);
        JsonNode second = client.messages().sendText(request);

        assertThat(first.get("ok").asBoolean()).isTrue();
        assertThat(second.get("ok").asBoolean()).isTrue();
      }

      RecordedRequest token = server.takeRequest();
      assertThat(token.getPath()).isEqualTo("/token");
      assertThat(token.getBody().readUtf8()).contains("client_token").contains("client_secret");

      RecordedRequest message = server.takeRequest();
      assertThat(message.getPath()).isEqualTo("/instances/VZ123/chat/send/text");
      assertThat(message.getHeader("Authorization")).isEqualTo("Bearer jwt-token");
      assertThat(message.getHeader("X-Client-Token")).isEqualTo("client-token");
      assertThat(message.getHeader("X-Instance-Token")).isEqualTo("instance-token");
      assertThat(message.getHeader("User-Agent")).startsWith("VZaps.SDK.Java/");
      assertThat(message.getBody().readUtf8())
          .contains("\"phone\":\"5511999999999\"")
          .contains("\"message\":\"Hello from Java\"")
          .doesNotContain("instance_token")
          .doesNotContain("instance_id");

      assertThat(server.takeRequest().getPath()).isEqualTo("/instances/VZ123/chat/send/text");
      assertThat(server.getRequestCount()).isEqualTo(3);
    }
  }

  @Test
  void instancesGetUsesPostBodyContract() throws Exception {
    try (MockWebServer server = new MockWebServer()) {
      server.enqueue(json("{\"accessToken\":\"jwt-token\",\"expiresIn\":3600}"));
      server.enqueue(json("{\"id\":\"VZ123\"}"));

      try (VZapsClient client = client(server)) {
        assertThat(client.instances().get("VZ123").get("id").asText()).isEqualTo("VZ123");
      }

      server.takeRequest();
      RecordedRequest request = server.takeRequest();
      assertThat(request.getMethod()).isEqualTo("POST");
      assertThat(request.getPath()).isEqualTo("/instances/get");
      assertThat(request.getBody().readUtf8()).isEqualTo("{\"id\":\"VZ123\"}");
    }
  }

  @Test
  void refreshesTokenOnceAfterUnauthorizedResponse() throws Exception {
    try (MockWebServer server = new MockWebServer()) {
      server.enqueue(json("{\"accessToken\":\"old-token\",\"expiresIn\":3600}"));
      server.enqueue(new MockResponse().setResponseCode(401).setBody("{\"message\":\"expired\"}"));
      server.enqueue(json("{\"accessToken\":\"new-token\",\"expiresIn\":3600}"));
      server.enqueue(json("{\"ok\":true}"));

      try (VZapsClient client = client(server)) {
        client.sessions().status("VZ123", InstanceRequestOptions.none());
      }

      server.takeRequest();
      RecordedRequest firstAttempt = server.takeRequest();
      server.takeRequest();
      RecordedRequest secondAttempt = server.takeRequest();
      assertThat(firstAttempt.getHeader("Authorization")).isEqualTo("Bearer old-token");
      assertThat(secondAttempt.getHeader("Authorization")).isEqualTo("Bearer new-token");
    }
  }

  @Test
  void mapsRateLimitErrors() throws Exception {
    try (MockWebServer server = new MockWebServer()) {
      server.enqueue(json("{\"accessToken\":\"jwt-token\",\"expiresIn\":3600}"));
      server.enqueue(
          new MockResponse()
              .setResponseCode(429)
              .setBody("{\"message\":\"slow down\",\"code\":\"rate_limit\"}"));

      try (VZapsClient client = client(server)) {
        assertThatThrownBy(() -> client.instances().list())
            .isInstanceOf(VZapsRateLimitException.class)
            .hasMessageContaining("slow down");
      }
    }
  }

  private static VZapsClient client(MockWebServer server) {
    return VZapsClient.builder()
        .clientToken("client-token")
        .clientSecret("client-secret")
        .baseUrl(server.url("/").toString())
        .build();
  }

  private static MockResponse json(String body) {
    return new MockResponse()
        .setResponseCode(200)
        .setHeader("Content-Type", "application/json")
        .setBody(body);
  }
}
