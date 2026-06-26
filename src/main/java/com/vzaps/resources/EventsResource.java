package com.vzaps.resources;

import com.vzaps.VZapsClientOptions;
import com.vzaps.http.VZapsHttpClient;
import com.vzaps.models.realtime.EventSubscribeRequest;
import com.vzaps.realtime.ReconnectPolicy;
import com.vzaps.realtime.VZapsEventSubscription;

public final class EventsResource extends BaseResource {
  private final VZapsClientOptions options;

  public EventsResource(VZapsHttpClient http, VZapsClientOptions options) {
    super(http);
    this.options = options;
  }

  public VZapsEventSubscription subscribe(EventSubscribeRequest request) {
    return new VZapsEventSubscription(http, options, request, ReconnectPolicy.DEFAULT);
  }

  public VZapsEventSubscription subscribe(EventSubscribeRequest request, ReconnectPolicy policy) {
    return new VZapsEventSubscription(http, options, request, policy);
  }
}
