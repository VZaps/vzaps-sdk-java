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
      var request =
          GenericInstanceRequest.builder()
              .instanceId(System.getenv("VZAPS_INSTANCE_ID"))
              .instanceToken(System.getenv("VZAPS_INSTANCE_TOKEN"))
              .put("phone", "5511999999999")
              .put("name", "Pick")
              .put("options", List.of("A", "B"))
              .build();
      System.out.println(client.messages().sendPoll(request));
    }
  }
}
