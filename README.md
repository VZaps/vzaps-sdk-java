# VZaps Java SDK

[![CI](https://github.com/VZaps/vzaps-sdk-java/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/VZaps/vzaps-sdk-java/actions/workflows/ci.yml) [![SDK Documentation](https://img.shields.io/badge/SDK-Documentation-blue)](https://docs.vzaps.com/en/sdk/java/installation) [![license](https://img.shields.io/badge/license-MIT-blue.svg)](./LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/com.vzaps/vzaps-sdk.svg)](https://central.sonatype.com/artifact/com.vzaps/vzaps-sdk)
[![Java](https://img.shields.io/badge/Java-11%2B-blue.svg)](https://www.oracle.com/java/)

Official Java client for the [VZaps public API](https://docs.vzaps.com). Send WhatsApp messages, manage instances, configure webhooks, and subscribe to realtime events with a resource-oriented interface and sync and async clients.

Works in **Java 11+**. HTTP uses the standard library `java.net.http.HttpClient`; WebSocket realtime uses the same client’s WebSocket support.

---

## Table of contents

- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Examples](#examples)
- [Quick start](#quick-start)
- [Authentication](#authentication)
- [Configuration](#configuration)
- [Resources](#resources)
- [Instance tokens](#instance-tokens)
- [Webhooks](#webhooks)
- [Realtime events](#realtime-events)
- [Error handling](#error-handling)
- [Java](#java)
- [Documentation](#documentation)

---

## Features

- **Automatic JWT handling** — exchanges `clientToken` + `clientSecret` for a bearer token and refreshes it before expiry.
- **Resource-oriented API** — `instances()`, `messages()`, `webhooks()`, `contacts()`, `groups()`, and `events()` mirror the public HTTP contract.
- **Sync and async clients** — `VZapsClient` and `VZapsAsyncClient` share the same resource surface.
- **Realtime WebSocket client** — subscribe to instance events with reconnect, resume (`lastEventId`), and server-side ack.
- **Instance token support** — pass `instanceToken` on each instance-scoped request.
- **Typed and dynamic payloads** — builders for common requests plus `GenericInstanceRequest` for new API fields.
- **Extensible transport** — inject a custom `HttpClient` for proxy, TLS, or testing.

---

## Requirements

| Runtime | Minimum version |
| --- | --- |
| Java | 11+ |

The SDK uses `java.net.http.HttpClient` by default. No extra HTTP dependency is required.

---

## Installation

Maven:

```xml
<dependency>
  <groupId>com.vzaps</groupId>
  <artifactId>vzaps-sdk</artifactId>
  <version>0.1.0</version>
</dependency>
```

Gradle Kotlin DSL:

```kotlin
dependencies {
  implementation("com.vzaps:vzaps-sdk:0.1.0")
}
```

---

## Examples

Runnable Maven projects live in [`examples/`](examples/). Each folder includes a `pom.xml` that depends on the published SDK from Maven Central.

```bash
cd examples/01-auth-and-list-instances
mvn compile exec:java
```

See [`examples/README.md`](examples/README.md) for the full list and environment variables.

---

## Quick start

Create credentials in the [VZaps dashboard](https://docs.vzaps.com) (`clientToken` and `clientSecret`), then send a text message:

```java
import com.vzaps.VZapsClient;
import com.vzaps.models.messages.SendTextMessageRequest;

try (VZapsClient client = VZapsClient.builder()
    .clientToken(System.getenv("VZAPS_CLIENT_TOKEN"))
    .clientSecret(System.getenv("VZAPS_CLIENT_SECRET"))
    .build()) {

  client.messages().sendText(SendTextMessageRequest.builder()
      .instanceId("VZKB8AU4S4CWY1SLXX4I5WJGRZQMDDFTV6")
      .instanceToken(System.getenv("VZAPS_INSTANCE_TOKEN"))
      .phone("5511999999999")
      .message("Hello from VZaps")
      .build());
}
```

Async equivalent:

```java
import com.vzaps.VZapsAsyncClient;

try (VZapsAsyncClient client = VZapsAsyncClient.builder()
    .clientToken(System.getenv("VZAPS_CLIENT_TOKEN"))
    .clientSecret(System.getenv("VZAPS_CLIENT_SECRET"))
    .build()) {

  client.instances().list()
      .thenAccept(System.out::println)
      .join();
}
```

---

## Authentication

VZaps uses a two-step model:

1. **Account credentials** — `clientToken` and `clientSecret` identify your integration. The SDK calls `POST /token` and caches the JWT.
2. **Instance token** — instance-scoped routes also require `X-Instance-Token`. Pass it on each instance-scoped request (see [Instance tokens](#instance-tokens)).

Every authenticated HTTP request sends:

| Header | Value |
| --- | --- |
| `Authorization` | `Bearer <jwt>` |
| `X-Client-Token` | Your client token |
| `X-Instance-Token` | Instance token, on instance-scoped requests |

You rarely need to call `auth().getAccessToken()` directly — resources attach the token for you. Use it when integrating with custom HTTP logic:

```java
String token = client.auth().getAccessToken();
```

---

## Configuration

The SDK connects to the VZaps production platform automatically:

| Service | Endpoint |
| --- | --- |
| REST API | `https://api.vzaps.com` |
| Realtime WebSocket | `wss://realtime.vzaps.com/events/ws` |

Pass options to `VZapsClient.builder()`:

| Option | Type | Default | Description |
| --- | --- | --- | --- |
| `clientToken` | `String` | — | **Required.** Public client token from the dashboard. |
| `clientSecret` | `String` | — | **Required.** Client secret used to obtain JWTs. |
| `baseUrl` | `URI` | `https://api.vzaps.com` | REST API base URL. |
| `realtimeUrl` | `URI` | `wss://realtime.vzaps.com` | Realtime WebSocket base URL. |
| `connectTimeout` | `Duration` | `10s` | TCP connect timeout. |
| `requestTimeout` | `Duration` | `30s` | HTTP request timeout. |
| `tokenRefreshSkew` | `Duration` | `1m` | Refresh JWT this long before expiry. |
| `userAgent` | `String` | package default | Optional `User-Agent` header on HTTP requests. |
| `httpClient` | `HttpClient` | — | Custom Java 11 `HttpClient` (proxy, TLS, tests). |

No host configuration is required — install the package, pass your credentials, and the client targets the production API and realtime service.

```java
VZapsClient client = VZapsClient.builder()
    .clientToken(clientToken)
    .clientSecret(clientSecret)
    .connectTimeout(Duration.ofSeconds(10))
    .requestTimeout(Duration.ofSeconds(30))
    .build();
```

---

## Resources

The client exposes namespaced resources. Responses return Jackson `JsonNode` for compatibility with the evolving public API. Use `request(...)` for typed responses and `rawRequest(...)` when you need the full HTTP response.

### `client.instances()`

| Method | HTTP | Description |
| --- | --- | --- |
| `create(request)` | `PUT /instances/create` | Create a WhatsApp instance. |
| `list(request?)` | `POST /instances/list` | List instances (pagination, search, sort). |
| `get(instanceId)` | `POST /instances/get` | Get instance details. |
| `update(instanceId, request, options?)` | `PATCH /instances/:id` | Update instance settings. |
| `restart(instanceId, options?)` | `POST /instances/:id/restart` | Restart instance runtime. |

### `client.messages()`

`client.messages()` wraps the public WhatsApp send and chat endpoints. The most common calls are shown below; the SDK also exposes the other public message operations documented in the API reference, including media, interactive messages, reactions, polls, downloads, edits, deletes, presence, and read receipts.

```java
client.messages().sendText(SendTextMessageRequest.builder()
    .instanceId("VZ...")
    .instanceToken("instance-token")
    .phone("5511999999999")
    .message("Hello")
    .build());

client.messages().sendImage(GenericInstanceRequest.builder()
    .instanceId("VZ...")
    .instanceToken("instance-token")
    .put("phone", "5511999999999")
    .put("image", "https://example.com/photo.jpg")
    .put("caption", "Check this out")
    .build());
```

Available send helpers include `sendText`, `sendImage`, `sendAudio`, `sendDocument`, `sendVideo`, `sendSticker`, `sendGif`, `sendLocation`, `sendContact`, `sendButtons`, `sendList`, `sendLink`, and `sendPoll`. See the API documentation for complete payload examples.

### `client.webhooks()`

| Method | HTTP | Description |
| --- | --- | --- |
| `get(instanceId, options?)` | `GET /instances/:id/webhook` | Read current webhook configuration. |
| `set(request)` | `POST /instances/:id/webhook` | Configure webhook URL and subscribed events. |

### `client.contacts()`

| Method | HTTP | Description |
| --- | --- | --- |
| `list(instanceId, options?)` | `GET /instances/:id/contact/list` | List contacts for the instance. |
| `add(request)` | `POST /instances/:id/contact/add` | Add a contact. |

### `client.groups()`

| Method | HTTP | Description |
| --- | --- | --- |
| `list(request)` | `GET /instances/:id/group/list` | List groups (paginated). |
| `get(request)` | `GET /instances/:id/group/info` | Get group metadata by `groupId`. |

### `client.sessions()`

| Method | HTTP | Description |
| --- | --- | --- |
| `status(instanceId, options)` | `GET /instances/:id/session/status` | Check WhatsApp login state and, when connected, live profile fields. |

`GET /instances/{id}/session/status` returns `SessionStatusResponse`. When `data().connected()` is `true`, `data()` includes (in order) `phone()`, `whatsappJid()`, `pushName()`, `businessName()`, `businessProfile()`, `profilePictureId()`, `profilePictureUrl()`, `profileUrl()`, and optional `verifiedName()`, `about()`, `website()`. When disconnected, `data()` only has `connected()` set to `false`.

Other public namespaces are available as first-class resources too: `sessions()`, `users()`, `queues()`, `typeBots()`, `chatwoot()`, and `chats()`.

### `client.request(...)` / `client.rawRequest(...)`

Escape hatch for advanced calls or newly released endpoints:

```java
import com.vzaps.http.VZapsHttpRequest;

JsonNode instance = client.request(VZapsHttpRequest.builder()
    .method("POST")
    .path("/instances/get")
    .body(Map.of("id", "VZ..."))
    .build());
```

---

## Instance tokens

Instance-scoped routes require the instance token in addition to account credentials. Pass it on each request that targets an instance:

```java
client.messages().sendText(SendTextMessageRequest.builder()
    .instanceId("VZ...")
    .instanceToken("instance-token")
    .phone("5511999999999")
    .message("Hello")
    .build());
```

---

## Webhooks

Configure HTTP callbacks for instance events (same payload shape as realtime `data`, delivered to your URL):

```java
client.webhooks().set(GenericInstanceRequest.builder()
    .instanceId("VZ...")
    .instanceToken("instance-token")
    .put("webhook_url", "https://example.com/webhooks/vzaps")
    .put("events", List.of("Message", "Connected", "Disconnected"))
    .build());
```

Common event types: `Message`, `ReadReceipt`, `Connected`, `Disconnected`, `Presence`, `ChatPresence`, `HistorySync`, `GroupParticipantsAdd`, `GroupParticipantsRemove`, or `All`.

Event payloads (webhook and realtime) use **snake_case**, matching the platform. Incoming media events include `media_url` inside `data` when platform storage is available.

---

## Realtime events

Subscribe to the same events over WebSocket at **`wss://realtime.vzaps.com`**. This is the recommended path for in-app notifications, bots, and dashboards that need low-latency delivery without exposing a public webhook URL.

### Subscribe

```java
import com.vzaps.models.realtime.EventSubscribeRequest;
import com.vzaps.models.realtime.VZapsEventType;

try (var subscription = client.events().subscribe(EventSubscribeRequest.builder()
    .instanceId("VZ...")
    .instanceToken("instance-token")
    .event(VZapsEventType.MESSAGE)
    .event(VZapsEventType.CONNECTED)
    .event(VZapsEventType.DISCONNECTED)
    .reconnect(true)
    .lastEventId("evt_previous_id") // optional resume after disconnect
    .build())) {

  subscription.on(VZapsEventType.MESSAGE, event -> System.out.println(event.data()));
  subscription.awaitClose();
}
```

### Event envelope

Each WebSocket message keeps the platform shape (`snake_case`):

```json
{
  "id": "evt_…",
  "type": "Message",
  "instance_id": "VZ…",
  "created_at": "2026-06-23T22:57:17.000Z",
  "data": {
    "type": "Message",
    "event": { },
    "media_url": "https://…"
  }
}
```

- **`data`** — same payload as webhook delivery (`snake_case`).
- **`media_url`** — present on incoming media messages when platform storage is available.

### Delivery and ack

Delivery is **at-least-once**. After your handler runs, the SDK sends an ack automatically on the WebSocket connection. Use `lastEventId` when reconnecting if you need to reduce gaps. Deduplicate on `event.id()` in your application if you process events idempotently.

### Subscribe options

| Option | Type | Default | Description |
| --- | --- | --- | --- |
| `instanceId` | `String` | — | **Required.** Instance to watch. |
| `events` | `VZapsEventType...` | all subscribed | Comma-filtered event types. |
| `instanceToken` | `String` | — | **Required.** Instance token for authorization. |
| `reconnect` | `boolean` | `true` | Reconnect after socket close. |
| `lastEventId` | `String` | — | Resume cursor after disconnect. |

Advanced reconnect timing uses `EventsResource.subscribe(request, ReconnectPolicy)` when you need custom backoff.

### Handler registration

| Method | When it fires |
| --- | --- |
| `on(VZapsEventType.MESSAGE, …)` | Matching realtime event type. |
| `on(VZapsEventType.ALL, …)` | Every event type. |

---

## Error handling

The SDK throws typed unchecked exceptions you can catch and branch on:

| Class | When |
| --- | --- |
| `VZapsException` | Base class for SDK failures. |
| `VZapsApiException` | Base for HTTP API errors; includes status, code, and details. |
| `VZapsAuthenticationException` | Invalid `clientToken` / `clientSecret` (401/403). |
| `VZapsRateLimitException` | Rate limited (429). |
| `VZapsTimeoutException` | Request exceeded `requestTimeout`. |
| `VZapsRealtimeException` | Realtime handler or transport failures. |

```java
import com.vzaps.exceptions.VZapsApiException;
import com.vzaps.exceptions.VZapsAuthenticationException;
import com.vzaps.exceptions.VZapsRateLimitException;
import com.vzaps.exceptions.VZapsTimeoutException;

try {
  client.messages().sendText(request);
} catch (VZapsAuthenticationException ex) {
  System.err.println("Check client credentials");
} catch (VZapsTimeoutException ex) {
  System.err.println("Request timed out");
} catch (VZapsRateLimitException ex) {
  System.err.println("Rate limited");
} catch (VZapsApiException ex) {
  System.err.println(ex.statusCode() + " " + ex.getMessage());
  throw ex;
}
```

Exception messages never include `clientSecret`, JWTs, or `instanceToken`.

---

## Java

The package uses **camelCase** for Java APIs and Jackson models. **Realtime and webhook event payloads stay in snake_case** so both delivery channels match the platform wire format.

Most instance-scoped resources accept `GenericInstanceRequest` so new public fields can be used immediately:

```java
client.messages().sendImage(GenericInstanceRequest.builder()
    .instanceId(instanceId)
    .instanceToken(instanceToken)
    .put("phone", "5511999999999")
    .put("image", "https://example.com/image.png")
    .put("caption", "Sent from Java")
    .build());
```

Decode responses into your own types with `client.request(method, path, body, options, YourType.class)`.

---

## Documentation

- [VZaps docs](https://docs.vzaps.com)
- [API reference (OpenAPI)](https://docs.vzaps.com/api-reference)
- [Postman collections](https://docs.vzaps.com/postman/)
- [Report an issue](https://github.com/VZaps/vzaps-sdk-java/issues)

---

## License

MIT © VZaps
