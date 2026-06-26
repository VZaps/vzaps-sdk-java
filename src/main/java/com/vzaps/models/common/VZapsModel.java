package com.vzaps.models.common;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** Base model that preserves forward-compatible public API fields. */
public class VZapsModel {
  @JsonIgnore private final Map<String, Object> additionalData = new LinkedHashMap<>();

  @JsonAnyGetter
  public Map<String, Object> additionalData() {
    return Collections.unmodifiableMap(additionalData);
  }

  @JsonAnySetter
  public void putAdditionalData(String key, Object value) {
    if (value != null) {
      additionalData.put(toSnakeCase(key), value);
    }
  }

  protected void putAllAdditionalData(Map<String, ?> values) {
    if (values != null) {
      values.forEach(this::putAdditionalData);
    }
  }

  private static String toSnakeCase(String value) {
    StringBuilder result = new StringBuilder(value.length() + 8);
    for (int i = 0; i < value.length(); i++) {
      char current = value.charAt(i);
      if (Character.isUpperCase(current)) {
        if (i > 0) {
          result.append('_');
        }
        result.append(Character.toLowerCase(current));
      } else {
        result.append(current);
      }
    }
    return result.toString();
  }
}
