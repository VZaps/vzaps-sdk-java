import com.vzaps.VZapsClient;
import com.vzaps.models.common.GenericInstanceRequest;
import java.util.List;
import java.util.Map;

public final class Main {
  public static void main(String[] args) {
    var base =
        GenericInstanceRequest.builder()
            .instanceId(System.getenv("VZAPS_INSTANCE_ID"))
            .instanceToken(System.getenv("VZAPS_INSTANCE_TOKEN"))
            .put("phone", "5511999999999");

    try (VZapsClient client =
        VZapsClient.builder()
            .clientToken(System.getenv("VZAPS_CLIENT_TOKEN"))
            .clientSecret(System.getenv("VZAPS_CLIENT_SECRET"))
            .build()) {
      System.out.println(
          client
              .messages()
              .sendImage(base.put("image", "https://example.com/image.png").put("caption", "Image").build()));
      System.out.println(
          client
              .messages()
              .sendButtons(
                  GenericInstanceRequest.builder()
                      .instanceId(System.getenv("VZAPS_INSTANCE_ID"))
                      .instanceToken(System.getenv("VZAPS_INSTANCE_TOKEN"))
                      .put("phone", "5511999999999")
                      .put("message", "Choose one")
                      .put("buttons", List.of(Map.of("id", "yes", "text", "Yes")))
                      .build()));
    }
  }
}
