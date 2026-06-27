import com.vzaps.VZapsClient;
import com.vzaps.models.common.InstanceCreateRequest;

public final class Main {
  public static void main(String[] args) {
    try (VZapsClient client =
        VZapsClient.builder()
            .clientToken(System.getenv("VZAPS_CLIENT_TOKEN"))
            .clientSecret(System.getenv("VZAPS_CLIENT_SECRET"))
            .build()) {
      System.out.println(
          client
              .instances()
              .create(InstanceCreateRequest.builder().name("Java SDK example").build()));
    }
  }
}
