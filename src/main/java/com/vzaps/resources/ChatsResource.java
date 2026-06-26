package com.vzaps.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.vzaps.models.common.GenericInstanceRequest;

public final class ChatsResource extends BaseResource {
  public ChatsResource(com.vzaps.http.VZapsHttpClient http) {
    super(http);
  }

  public JsonNode list(GenericInstanceRequest request) {
    return send(
        "GET",
        "/instances/" + esc(request.instanceId()) + "/chats",
        null,
        request.instanceToken(),
        query("page", request.additionalData().get("page"), "pageSize", request.additionalData().get("pageSize")));
  }

  public JsonNode get(GenericInstanceRequest request, String phone) {
    return action("GET", request, phone, "");
  }

  public JsonNode archive(GenericInstanceRequest request, String phone) {
    return action("POST", request, phone, "/archive");
  }

  public JsonNode unarchive(GenericInstanceRequest request, String phone) {
    return action("POST", request, phone, "/unarchive");
  }

  public JsonNode mute(GenericInstanceRequest request, String phone) {
    return action("POST", request, phone, "/mute");
  }

  public JsonNode unmute(GenericInstanceRequest request, String phone) {
    return action("POST", request, phone, "/unmute");
  }

  public JsonNode pin(GenericInstanceRequest request, String phone) {
    return action("POST", request, phone, "/pin");
  }

  public JsonNode unpin(GenericInstanceRequest request, String phone) {
    return action("POST", request, phone, "/unpin");
  }

  public JsonNode read(GenericInstanceRequest request, String phone) {
    return action("POST", request, phone, "/read");
  }

  public JsonNode unread(GenericInstanceRequest request, String phone) {
    return action("POST", request, phone, "/unread");
  }

  public JsonNode clear(GenericInstanceRequest request, String phone) {
    return action("POST", request, phone, "/clear");
  }

  public JsonNode delete(GenericInstanceRequest request, String phone) {
    return action("DELETE", request, phone, "");
  }

  public JsonNode setExpiration(GenericInstanceRequest request, String phone) {
    return action("PUT", request, phone, "/expiration");
  }

  private JsonNode action(String method, GenericInstanceRequest request, String phone, String suffix) {
    return send(
        method,
        "/instances/" + esc(request.instanceId()) + "/chats/" + esc(phone) + suffix,
        request);
  }
}
