package com.vzaps.models.common;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class InstanceListRequest extends VZapsModel {
  private final Integer page;
  private final Integer size;
  private final Integer pageSize;
  private final Map<String, Object> filter;
  private final String search;
  private final String sort;
  private final Boolean sortDesc;

  private InstanceListRequest(Builder builder) {
    this.page = builder.page;
    this.size = builder.size;
    this.pageSize = builder.pageSize;
    this.filter = Collections.unmodifiableMap(new LinkedHashMap<>(builder.filter));
    this.search = builder.search;
    this.sort = builder.sort;
    this.sortDesc = builder.sortDesc;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Integer page() {
    return page;
  }

  public Integer size() {
    return size;
  }

  public Integer pageSize() {
    return pageSize;
  }

  public Map<String, Object> filter() {
    return filter;
  }

  public String search() {
    return search;
  }

  public String sort() {
    return sort;
  }

  public Boolean sortDesc() {
    return sortDesc;
  }

  public static final class Builder {
    private Integer page;
    private Integer size;
    private Integer pageSize;
    private final Map<String, Object> filter = new LinkedHashMap<>();
    private String search;
    private String sort;
    private Boolean sortDesc;

    public Builder page(Integer page) {
      this.page = page;
      return this;
    }

    public Builder size(Integer size) {
      this.size = size;
      return this;
    }

    public Builder pageSize(Integer pageSize) {
      this.pageSize = pageSize;
      return this;
    }

    public Builder filter(String key, Object value) {
      if (value != null) {
        filter.put(key, value);
      }
      return this;
    }

    public Builder filter(Map<String, ?> values) {
      if (values != null) {
        values.forEach(this::filter);
      }
      return this;
    }

    public Builder search(String search) {
      this.search = search;
      return this;
    }

    public Builder sort(String sort) {
      this.sort = sort;
      return this;
    }

    public Builder sortDesc(Boolean sortDesc) {
      this.sortDesc = sortDesc;
      return this;
    }

    public InstanceListRequest build() {
      return new InstanceListRequest(this);
    }
  }
}
