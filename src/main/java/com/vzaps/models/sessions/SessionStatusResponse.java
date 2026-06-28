package com.vzaps.models.sessions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vzaps.models.common.VZapsModel;
import java.util.List;
import java.util.Map;

public final class SessionStatusResponse extends VZapsModel {
  @JsonProperty("code")
  private int code;

  @JsonProperty("success")
  private boolean success;

  @JsonProperty("data")
  private SessionStatusData data;

  public int code() {
    return code;
  }

  public boolean success() {
    return success;
  }

  public SessionStatusData data() {
    return data;
  }

  public static final class SessionStatusData extends VZapsModel {
    @JsonProperty("connected")
    private boolean connected;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("whatsapp_jid")
    private String whatsappJid;

    @JsonProperty("push_name")
    private String pushName;

    @JsonProperty("business_name")
    private String businessName;

    @JsonProperty("business_profile")
    private SessionBusinessProfile businessProfile;

    @JsonProperty("profile_picture_id")
    private String profilePictureId;

    @JsonProperty("profile_picture_url")
    private String profilePictureUrl;

    @JsonProperty("profile_url")
    private String profileUrl;

    @JsonProperty("verified_name")
    private String verifiedName;

    @JsonProperty("about")
    private String about;

    @JsonProperty("website")
    private String website;

    public boolean connected() {
      return connected;
    }

    public String phone() {
      return phone;
    }

    public String whatsappJid() {
      return whatsappJid;
    }

    public String pushName() {
      return pushName;
    }

    public String businessName() {
      return businessName;
    }

    public SessionBusinessProfile businessProfile() {
      return businessProfile;
    }

    public String profilePictureId() {
      return profilePictureId;
    }

    public String profilePictureUrl() {
      return profilePictureUrl;
    }

    public String profileUrl() {
      return profileUrl;
    }

    public String verifiedName() {
      return verifiedName;
    }

    public String about() {
      return about;
    }

    public String website() {
      return website;
    }
  }

  public static final class SessionBusinessProfile extends VZapsModel {
    @JsonProperty("business_hours_timezone")
    private String businessHoursTimezone;

    @JsonProperty("categories")
    private List<SessionBusinessCategory> categories;

    @JsonProperty("profile_options")
    private Map<String, String> profileOptions;

    @JsonProperty("address")
    private String address;

    @JsonProperty("email")
    private String email;

    public String businessHoursTimezone() {
      return businessHoursTimezone;
    }

    public List<SessionBusinessCategory> categories() {
      return categories;
    }

    public Map<String, String> profileOptions() {
      return profileOptions;
    }

    public String address() {
      return address;
    }

    public String email() {
      return email;
    }
  }

  public static final class SessionBusinessCategory extends VZapsModel {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    public String id() {
      return id;
    }

    public String name() {
      return name;
    }
  }
}
