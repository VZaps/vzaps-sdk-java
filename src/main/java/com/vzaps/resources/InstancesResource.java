package com.vzaps.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.vzaps.models.common.InstanceCreateRequest;
import com.vzaps.models.common.InstanceListRequest;
import com.vzaps.models.common.InstanceRequestOptions;
import java.util.LinkedHashMap;
import java.util.Map;

public final class InstancesResource extends BaseResource {
  public InstancesResource(com.vzaps.http.VZapsHttpClient http) {
    super(http);
  }

  public JsonNode create(InstanceCreateRequest request) {
    return send("PUT", "/instances/create", request, (String) null);
  }

  public JsonNode list() {
    return list(InstanceListRequest.builder().build());
  }

  public JsonNode list(InstanceListRequest request) {
    Map<String, Object> filter = new LinkedHashMap<>(request.filter());
    if (filter.isEmpty() && request.search() != null && !request.search().trim().isEmpty()) {
      filter.put("query", request.search().trim());
    }
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("page", request.page() == null ? 1 : request.page());
    body.put(
        "size",
        request.size() == null
            ? (request.pageSize() == null ? 20 : request.pageSize())
            : request.size());
    body.put("filter", filter);
    if (request.sort() != null) {
      body.put("sort", request.sort());
    }
    if (request.sortDesc() != null) {
      body.put("sortDesc", request.sortDesc());
    }
    return send("POST", "/instances/list", body, (String) null);
  }

  public JsonNode get(String instanceId) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("id", instanceId);
    return send("POST", "/instances/get", body, (String) null);
  }

  public JsonNode update(String instanceId, Object request, InstanceRequestOptions options) {
    return send("PATCH", "/instances/" + esc(instanceId), request, options);
  }

  public JsonNode restart(String instanceId, InstanceRequestOptions options) {
    return send("POST", "/instances/" + esc(instanceId) + "/restart", null, options);
  }

  public JsonNode delete(String instanceId, InstanceRequestOptions options) {
    return send("DELETE", "/instances/" + esc(instanceId), null, options);
  }

  public JsonNode provision(InstanceCreateRequest request) {
    return send("PUT", "/instances/provision", request, (String) null);
  }

  public JsonNode search(Object request) {
    return send("POST", "/instances/search", request, (String) null);
  }

  public JsonNode subscribe(String instanceId, Object request, InstanceRequestOptions options) {
    return send("POST", "/instances/" + esc(instanceId) + "/subscribe", request, options);
  }

  public JsonNode resumeSubscription(String instanceId, InstanceRequestOptions options) {
    return send("POST", "/instances/" + esc(instanceId) + "/resume-subscription", null, options);
  }

  public JsonNode cancel(String instanceId, InstanceRequestOptions options) {
    return send("PUT", "/instances/" + esc(instanceId) + "/cancel", null, options);
  }
}
