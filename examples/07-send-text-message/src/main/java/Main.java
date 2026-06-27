import com.vzaps.VZapsClient;
import com.vzaps.models.messages.SendTextMessageRequest;

public final class Main {
  public static void main(String[] args) {
    try (VZapsClient client =
        VZapsClient.builder()
            .clientToken(System.getenv("VZAPS_CLIENT_TOKEN"))
            .clientSecret(System.getenv("VZAPS_CLIENT_SECRET"))
            .build()) {
      System.out.println(
          client
              .messages()
              .sendText(
                  SendTextMessageRequest.builder()
                      .instanceId(System.getenv("VZAPS_INSTANCE_ID"))
                      .instanceToken(System.getenv("VZAPS_INSTANCE_TOKEN"))
                      .phone("5511999999999")
                      .message("Hello from Java")
                      .build()));
    }
  }
}
