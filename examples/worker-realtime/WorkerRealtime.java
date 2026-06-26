import com.vzaps.VZapsClient;
import com.vzaps.models.realtime.EventSubscribeRequest;
import com.vzaps.models.realtime.VZapsEventType;

public final class WorkerRealtime {
  public static void main(String[] args) throws Exception {
    try (VZapsClient client =
        VZapsClient.builder()
            .clientToken(env("VZAPS_CLIENT_TOKEN"))
            .clientSecret(env("VZAPS_CLIENT_SECRET"))
            .build()) {
      try (var subscription =
          client
              .events()
              .subscribe(
                  EventSubscribeRequest.builder()
                      .instanceId(env("VZAPS_INSTANCE_ID"))
                      .instanceToken(env("VZAPS_INSTANCE_TOKEN"))
                      .event(VZapsEventType.MESSAGE)
                      .event(VZapsEventType.CONNECTED)
                      .reconnect(true)
                      .build())) {
        subscription.on(VZapsEventType.ALL, event -> System.out.println(event.type() + " " + event.id()));
        subscription.awaitClose();
      }
    }
  }

  private static String env(String name) {
    String value = System.getenv(name);
    if (value == null || value.trim().isEmpty()) {
      throw new IllegalStateException(name + " is required.");
    }
    return value;
  }
}
