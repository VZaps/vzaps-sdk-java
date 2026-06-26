import com.vzaps.VZapsClient;
import com.vzaps.models.common.GenericInstanceRequest;
import java.util.List;

public final class Main {
  public static void main(String[] args) {
    try (VZapsClient client =
        VZapsClient.builder()
            .clientToken(System.getenv("VZAPS_CLIENT_TOKEN"))
            .clientSecret(System.getenv("VZAPS_CLIENT_SECRET"))
            .build()) {
      System.out.println(
          client
              .webhooks()
              .set(
                  GenericInstanceRequest.builder()
                      .instanceId(System.getenv("VZAPS_INSTANCE_ID"))
                      .instanceToken(System.getenv("VZAPS_INSTANCE_TOKEN"))
                      .put("url", "https://example.com/webhook")
                      .put("events", List.of("message", "connected"))
                      .build()));
    }
  }
}
