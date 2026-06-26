package com.vzaps.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.vzaps.models.common.GenericInstanceRequest;
import com.vzaps.models.common.InstanceScopedRequest;
import com.vzaps.models.messages.SendTextMessageRequest;

public final class MessagesResource extends BaseResource {
  public MessagesResource(com.vzaps.http.VZapsHttpClient http) {
    super(http);
  }

  public JsonNode sendText(SendTextMessageRequest request) {
    return message("POST", "/chat/send/text", request);
  }

  public JsonNode sendImage(GenericInstanceRequest request) {
    return message("POST", "/chat/send/image", request);
  }

  public JsonNode sendAudio(GenericInstanceRequest request) {
    return message("POST", "/chat/send/audio", request);
  }

  public JsonNode sendDocument(GenericInstanceRequest request) {
    return message("POST", "/chat/send/document", request);
  }

  public JsonNode sendVideo(GenericInstanceRequest request) {
    return message("POST", "/chat/send/video", request);
  }

  public JsonNode sendSticker(GenericInstanceRequest request) {
    return message("POST", "/chat/send/sticker", request);
  }

  public JsonNode sendGif(GenericInstanceRequest request) {
    return message("POST", "/chat/send/gif", request);
  }

  public JsonNode sendLocation(GenericInstanceRequest request) {
    return message("POST", "/chat/send/location", request);
  }

  public JsonNode sendContact(GenericInstanceRequest request) {
    return message("POST", "/chat/send/contact", request);
  }

  public JsonNode sendButtons(GenericInstanceRequest request) {
    return message("POST", "/chat/send/buttons", request);
  }

  public JsonNode sendList(GenericInstanceRequest request) {
    return message("POST", "/chat/send/list", request);
  }

  public JsonNode sendLink(GenericInstanceRequest request) {
    return message("POST", "/chat/send/link", request);
  }

  public JsonNode sendPoll(GenericInstanceRequest request) {
    return message("POST", "/chat/send/poll", request);
  }

  public JsonNode pollVote(GenericInstanceRequest request) {
    return message("POST", "/chat/poll/vote", request);
  }

  public JsonNode react(GenericInstanceRequest request) {
    return message("POST", "/chat/react", request);
  }

  public JsonNode removeReaction(GenericInstanceRequest request) {
    return message("DELETE", "/chat/react", request);
  }

  public JsonNode presence(GenericInstanceRequest request) {
    return message("POST", "/chat/presence", request);
  }

  public JsonNode markRead(GenericInstanceRequest request) {
    return message("POST", "/chat/markread", request);
  }

  public JsonNode edit(GenericInstanceRequest request, String messageId) {
    return super.send(
        "PATCH",
        "/instances/" + esc(request.instanceId()) + "/chat/messages/" + esc(messageId),
        bodyWithoutInstance(request),
        request.instanceToken());
  }

  public JsonNode delete(GenericInstanceRequest request, String messageId) {
    return super.send(
        "DELETE",
        "/instances/" + esc(request.instanceId()) + "/chat/messages/" + esc(messageId),
        bodyWithoutInstance(request),
        request.instanceToken());
  }

  public JsonNode downloadImage(GenericInstanceRequest request) {
    return message("POST", "/chat/downloadimage", request);
  }

  public JsonNode downloadVideo(GenericInstanceRequest request) {
    return message("POST", "/chat/downloadvideo", request);
  }

  public JsonNode downloadAudio(GenericInstanceRequest request) {
    return message("POST", "/chat/downloadaudio", request);
  }

  public JsonNode downloadDocument(GenericInstanceRequest request) {
    return message("POST", "/chat/downloaddocument", request);
  }

  public JsonNode send(String instanceId, String path, Object body, String instanceToken) {
    return super.send(
        "POST",
        "/instances/" + esc(instanceId) + "/chat/" + path.replaceFirst("^/+", ""),
        body,
        instanceToken);
  }

  private JsonNode message(String method, String suffix, InstanceScopedRequest request) {
    return super.send(
        method,
        "/instances/" + esc(request.instanceId()) + suffix,
        bodyWithoutInstance(request),
        request.instanceToken());
  }
}
