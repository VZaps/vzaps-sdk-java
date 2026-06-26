package com.vzaps.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vzaps.VZapsRequestOptions;
import com.vzaps.http.VZapsHttpClient;
import com.vzaps.json.VZapsJson;
import com.vzaps.models.common.InstanceRequestOptions;
import com.vzaps.models.common.InstanceScopedRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

abstract class BaseResource {
  protected final VZapsHttpClient http;

  BaseResource(VZapsHttpClient http) {
    this.http = http;
  }

  protected JsonNode send(String method, String path, Object body, String instanceToken) {
    return http.request(
        method,
        path,
        body,
        VZapsRequestOptions.builder().instanceToken(instanceToken).build(),
        JsonNode.class);
  }

  protected JsonNode send(
      String method, String path, Object body, String instanceToken, Map<String, Object> query) {
    return http.request(
        method,
        path,
        body,
        VZapsRequestOptions.builder().instanceToken(instanceToken).query(query).build(),
        JsonNode.class);
  }

  protected JsonNode send(String method, String path, Object body, InstanceRequestOptions options) {
    return send(method, path, body, options == null ? null : options.instanceToken());
  }

  protected JsonNode send(String method, String path, InstanceScopedRequest request) {
    return send(method, path, bodyWithoutInstance(request), request.instanceToken());
  }

  protected JsonNode send(
      String method, String path, InstanceScopedRequest request, String... excluded) {
    return send(method, path, bodyWithoutInstance(request, excluded), request.instanceToken());
  }

  protected static String esc(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
  }

  protected static ObjectNode bodyWithoutInstance(InstanceScopedRequest request, String... excluded) {
    ObjectNode body = VZapsJson.valueToObjectNode(request);
    body.remove("instance_id");
    body.remove("instance_token");
    if (excluded != null) {
      for (String item : excluded) {
        body.remove(snakeCase(item));
      }
    }
    return body;
  }

  protected static Map<String, Object> query(Object... values) {
    Map<String, Object> query = new LinkedHashMap<>();
    for (int i = 0; i + 1 < values.length; i += 2) {
      if (values[i + 1] != null) {
        query.put(String.valueOf(values[i]), values[i + 1]);
      }
    }
    return query;
  }

  private static String snakeCase(String value) {
    StringBuilder result = new StringBuilder(value.length() + 8);
    for (int i = 0; i < value.length(); i++) {
      char current = value.charAt(i);
      if (Character.isUpperCase(current)) {
        if (i > 0) {
          result.append('_');
        }
        result.append(Character.toLowerCase(current));
      } else {
        result.append(String.valueOf(current).toLowerCase(Locale.ROOT));
      }
    }
    return result.toString();
  }
}
