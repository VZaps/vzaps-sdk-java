import com.vzaps.VZapsClient;
import com.vzaps.models.realtime.EventSubscribeRequest;
import com.vzaps.models.realtime.VZapsEventType;

public final class Main {
  public static void main(String[] args) throws Exception {
    try (VZapsClient client =
        VZapsClient.builder()
            .clientToken(System.getenv("VZAPS_CLIENT_TOKEN"))
            .clientSecret(System.getenv("VZAPS_CLIENT_SECRET"))
            .build();
        var subscription =
            client
                .events()
                .subscribe(
                    EventSubscribeRequest.builder()
                        .instanceId(System.getenv("VZAPS_INSTANCE_ID"))
                        .instanceToken(System.getenv("VZAPS_INSTANCE_TOKEN"))
                        .event(VZapsEventType.MESSAGE)
                        .reconnect(true)
                        .build())) {
      subscription.on(VZapsEventType.ALL, event -> System.out.println(event.type() + " " + event.id()));
      subscription.awaitClose();
    }
  }
}
