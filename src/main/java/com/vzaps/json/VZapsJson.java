package com.vzaps.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vzaps.exceptions.VZapsException;

/** Shared JSON configuration for public API payloads. */
public final class VZapsJson {
  private static final ObjectMapper MAPPER =
      new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .setSerializationInclusion(JsonInclude.Include.NON_NULL)
          .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
          .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  private VZapsJson() {}

  public static ObjectMapper mapper() {
    return MAPPER;
  }

  public static String write(Object value) {
    try {
      return MAPPER.writeValueAsString(value);
    } catch (JsonProcessingException ex) {
      throw new VZapsException("Failed to serialize VZaps request body.", ex);
    }
  }

  public static <T> T read(String value, Class<T> type) {
    try {
      return MAPPER.readValue(value, type);
    } catch (JsonProcessingException ex) {
      throw new VZapsException("Failed to deserialize VZaps response body.", ex);
    }
  }

  public static JsonNode readTree(String value) {
    try {
      return MAPPER.readTree(value);
    } catch (JsonProcessingException ex) {
      throw new VZapsException("Failed to deserialize VZaps response body.", ex);
    }
  }

  public static ObjectNode objectNode() {
    return MAPPER.createObjectNode();
  }

  public static ObjectNode valueToObjectNode(Object value) {
    JsonNode node = MAPPER.valueToTree(value);
    if (!node.isObject()) {
      return objectNode();
    }
    return (ObjectNode) node;
  }
}
