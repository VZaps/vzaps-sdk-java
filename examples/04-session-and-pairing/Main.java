import com.vzaps.VZapsClient;
import com.vzaps.models.common.InstanceRequestOptions;

public final class Main {
  public static void main(String[] args) {
    String instanceId = System.getenv("VZAPS_INSTANCE_ID");
    var options =
        InstanceRequestOptions.builder().instanceToken(System.getenv("VZAPS_INSTANCE_TOKEN")).build();

    try (VZapsClient client =
        VZapsClient.builder()
            .clientToken(System.getenv("VZAPS_CLIENT_TOKEN"))
            .clientSecret(System.getenv("VZAPS_CLIENT_SECRET"))
            .build()) {
      System.out.println(client.sessions().status(instanceId, options));
      System.out.println(client.sessions().qr(instanceId, options));
      System.out.println(client.sessions().pairCode(instanceId, "5511999999999", options));
    }
  }
}
