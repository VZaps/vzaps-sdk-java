package com.vzaps.models.realtime;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class VZapsEvent {
  private String id;
  private VZapsEventType type = VZapsEventType.UNKNOWN;
  private String instanceId;
  private Instant createdAt;
  private JsonNode data;
  @JsonIgnore private final Map<String, JsonNode> additionalData = new LinkedHashMap<>();

  public String id() {
    return id;
  }

  public VZapsEventType type() {
    return type;
  }

  public String instanceId() {
    return instanceId;
  }

  public Instant createdAt() {
    return createdAt;
  }

  public JsonNode data() {
    return data;
  }

  @JsonAnyGetter
  public Map<String, JsonNode> additionalData() {
    return Collections.unmodifiableMap(additionalData);
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setType(VZapsEventType type) {
    this.type = type == null ? VZapsEventType.UNKNOWN : type;
  }

  public void setInstanceId(String instanceId) {
    this.instanceId = instanceId;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public void setData(JsonNode data) {
    this.data = data;
  }

  @JsonAnySetter
  public void putAdditionalData(String key, JsonNode value) {
    additionalData.put(key, value);
  }
}
