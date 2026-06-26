package com.vzaps;

import com.fasterxml.jackson.databind.JsonNode;
import com.vzaps.http.VZapsHttpClient;
import com.vzaps.http.VZapsHttpRequest;
import com.vzaps.resources.AuthResource;
import com.vzaps.resources.ChatwootResource;
import com.vzaps.resources.ChatsResource;
import com.vzaps.resources.ContactsResource;
import com.vzaps.resources.EventsResource;
import com.vzaps.resources.GroupsResource;
import com.vzaps.resources.InstancesResource;
import com.vzaps.resources.MessagesResource;
import com.vzaps.resources.QueuesResource;
import com.vzaps.resources.SessionsResource;
import com.vzaps.resources.TypeBotsResource;
import com.vzaps.resources.UsersResource;
import com.vzaps.resources.WebhooksResource;

public final class VZapsClient implements AutoCloseable {
  private final VZapsClientOptions options;
  private final VZapsHttpClient http;
  private final AuthResource auth;
  private final InstancesResource instances;
  private final SessionsResource sessions;
  private final MessagesResource messages;
  private final WebhooksResource webhooks;
  private final ContactsResource contacts;
  private final GroupsResource groups;
  private final UsersResource users;
  private final QueuesResource queues;
  private final TypeBotsResource typeBots;
  private final ChatwootResource chatwoot;
  private final ChatsResource chats;
  private final EventsResource events;

  public VZapsClient(VZapsClientOptions options) {
    this.options = options;
    this.http = new VZapsHttpClient(options);
    this.auth = new AuthResource(http);
    this.instances = new InstancesResource(http);
    this.sessions = new SessionsResource(http);
    this.messages = new MessagesResource(http);
    this.webhooks = new WebhooksResource(http);
    this.contacts = new ContactsResource(http);
    this.groups = new GroupsResource(http);
    this.users = new UsersResource(http);
    this.queues = new QueuesResource(http);
    this.typeBots = new TypeBotsResource(http);
    this.chatwoot = new ChatwootResource(http);
    this.chats = new ChatsResource(http);
    this.events = new EventsResource(http, options);
  }

  public static VZapsClientBuilder builder() {
    return new VZapsClientBuilder();
  }

  public VZapsClientOptions options() {
    return options;
  }

  public AuthResource auth() {
    return auth;
  }

  public InstancesResource instances() {
    return instances;
  }

  public SessionsResource sessions() {
    return sessions;
  }

  public MessagesResource messages() {
    return messages;
  }

  public WebhooksResource webhooks() {
    return webhooks;
  }

  public ContactsResource contacts() {
    return contacts;
  }

  public GroupsResource groups() {
    return groups;
  }

  public UsersResource users() {
    return users;
  }

  public QueuesResource queues() {
    return queues;
  }

  public TypeBotsResource typeBots() {
    return typeBots;
  }

  public ChatwootResource chatwoot() {
    return chatwoot;
  }

  public ChatsResource chats() {
    return chats;
  }

  public EventsResource events() {
    return events;
  }

  public <T> T request(String method, String path, Object body, VZapsRequestOptions options, Class<T> type) {
    return http.request(method, path, body, options, type);
  }

  public JsonNode request(VZapsHttpRequest request) {
    return http.request(request.method(), request.path(), request.body(), request.options(), JsonNode.class);
  }

  public VZapsResponse rawRequest(VZapsHttpRequest request) {
    return http.rawRequest(request.method(), request.path(), request.body(), request.options());
  }

  @Override
  public void close() {
    // java.net.http.HttpClient has no close method in Java 11.
  }
}
