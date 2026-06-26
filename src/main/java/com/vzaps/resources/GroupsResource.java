package com.vzaps.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.vzaps.models.common.GenericInstanceRequest;

public final class GroupsResource extends BaseResource {
  public GroupsResource(com.vzaps.http.VZapsHttpClient http) {
    super(http);
  }

  public JsonNode list(GenericInstanceRequest request) {
    return send(
        "GET",
        "/instances/" + esc(request.instanceId()) + "/group/list",
        null,
        request.instanceToken(),
        query(
            "page",
            request.additionalData().get("page"),
            "pageSize",
            request.additionalData().get("pageSize")));
  }

  public JsonNode get(GenericInstanceRequest request, String groupId) {
    return send(
        "GET",
        "/instances/" + esc(request.instanceId()) + "/group/info",
        null,
        request.instanceToken(),
        query("groupId", groupId));
  }

  public JsonNode inviteLink(GenericInstanceRequest request, String groupId, Boolean reset) {
    return send(
        "GET",
        "/instances/" + esc(request.instanceId()) + "/group/invitelink",
        null,
        request.instanceToken(),
        query("groupId", groupId, "reset", reset));
  }

  public JsonNode setPhoto(GenericInstanceRequest request) {
    return post("/group/photo", request);
  }

  public JsonNode setName(GenericInstanceRequest request) {
    return post("/group/name", request);
  }

  public JsonNode setDescription(GenericInstanceRequest request) {
    return post("/group/description", request);
  }

  public JsonNode setSettings(GenericInstanceRequest request) {
    return post("/group/settings", request);
  }

  public JsonNode create(GenericInstanceRequest request) {
    return post("/group/create", request);
  }

  public JsonNode addAdmin(GenericInstanceRequest request) {
    return post("/group/add-admin", request);
  }

  public JsonNode removeAdmin(GenericInstanceRequest request) {
    return post("/group/remove-admin", request);
  }

  private JsonNode post(String suffix, GenericInstanceRequest request) {
    return send("POST", "/instances/" + esc(request.instanceId()) + suffix, request);
  }
}
