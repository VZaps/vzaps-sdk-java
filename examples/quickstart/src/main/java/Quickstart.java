import com.vzaps.VZapsClient;
import com.vzaps.models.messages.SendTextMessageRequest;

public final class Quickstart {
  public static void main(String[] args) {
    String clientToken = env("VZAPS_CLIENT_TOKEN");
    String clientSecret = env("VZAPS_CLIENT_SECRET");
    String instanceId = env("VZAPS_INSTANCE_ID");
    String instanceToken = env("VZAPS_INSTANCE_TOKEN");

    try (VZapsClient client =
        VZapsClient.builder().clientToken(clientToken).clientSecret(clientSecret).build()) {
      System.out.println(client.instances().list());

      client
          .messages()
          .sendText(
              SendTextMessageRequest.builder()
                  .instanceId(instanceId)
                  .instanceToken(instanceToken)
                  .phone("5511999999999")
                  .message("Hello from the VZaps Java SDK")
                  .build());
    }
  }

  private static String env(String name) {
    String value = System.getenv(name);
    if (value == null || value.trim().isEmpty()) {
      throw new IllegalStateException(name + " is required.");
    }
    return value;
  }
}
