import com.vzaps.VZapsClient;
import com.vzaps.models.common.InstanceRequestOptions;
import java.util.Map;

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
              .subscribe(
                  System.getenv("VZAPS_INSTANCE_ID"),
                  Map.of("billing", "direct"),
                  InstanceRequestOptions.builder()
                      .instanceToken(System.getenv("VZAPS_INSTANCE_TOKEN"))
                      .build()));
    }
  }
}
