import com.vzaps.VZapsClient;
import com.vzaps.models.common.GenericInstanceRequest;

public final class Main {
  public static void main(String[] args) {
    try (VZapsClient client =
        VZapsClient.builder()
            .clientToken(System.getenv("VZAPS_CLIENT_TOKEN"))
            .clientSecret(System.getenv("VZAPS_CLIENT_SECRET"))
            .build()) {
      System.out.println(
          client.typeBots().list(System.getenv("VZAPS_INSTANCE_ID"), com.vzaps.models.common.InstanceRequestOptions.none()));
      System.out.println(
          client
              .chatwoot()
              .set(
                  GenericInstanceRequest.builder()
                      .instanceId(System.getenv("VZAPS_INSTANCE_ID"))
                      .instanceToken(System.getenv("VZAPS_INSTANCE_TOKEN"))
                      .put("url", "https://chatwoot.example.com")
                      .put("token", "chatwoot-token")
                      .build()));
    }
  }
}
