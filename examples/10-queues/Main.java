import com.vzaps.VZapsClient;
import com.vzaps.models.common.GenericInstanceRequest;

public final class Main {
  public static void main(String[] args) {
    var request =
        GenericInstanceRequest.builder()
            .instanceId(System.getenv("VZAPS_INSTANCE_ID"))
            .instanceToken(System.getenv("VZAPS_INSTANCE_TOKEN"))
            .build();

    try (VZapsClient client =
        VZapsClient.builder()
            .clientToken(System.getenv("VZAPS_CLIENT_TOKEN"))
            .clientSecret(System.getenv("VZAPS_CLIENT_SECRET"))
            .build()) {
      System.out.println(client.queues().listMessages(request));
      System.out.println(client.queues().listOperations(request));
    }
  }
}
