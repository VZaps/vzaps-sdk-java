# VZaps Java SDK Examples

Runnable Maven projects that consume the published SDK from Maven Central (`com.vzaps:vzaps-sdk`).

Each numbered folder is a standalone module with its own `pom.xml`. You can copy a single example elsewhere and run it on its own, or run everything from this directory.

## Prerequisites

- JDK 11 or later
- Maven 3.8+

Set these environment variables before running:

```bash
export VZAPS_CLIENT_TOKEN=your-client-token
export VZAPS_CLIENT_SECRET=your-client-secret
export VZAPS_INSTANCE_ID=VZ...
export VZAPS_INSTANCE_TOKEN=your-instance-token
```

On Windows PowerShell:

```powershell
$env:VZAPS_CLIENT_TOKEN="your-client-token"
$env:VZAPS_CLIENT_SECRET="your-client-secret"
$env:VZAPS_INSTANCE_ID="VZ..."
$env:VZAPS_INSTANCE_TOKEN="your-instance-token"
```

## Run one example

```bash
cd examples/01-auth-and-list-instances
mvn compile exec:java
```

Other examples:

```bash
cd examples/02-create-instance && mvn compile exec:java
cd examples/03-instance-subscription && mvn compile exec:java
cd examples/04-session-and-pairing && mvn compile exec:java
cd examples/05-configure-webhook && mvn compile exec:java
cd examples/06-realtime-subscribe && mvn compile exec:java
cd examples/07-send-text-message && mvn compile exec:java
cd examples/08-send-media-and-interactive && mvn compile exec:java
cd examples/09-send-poll-reaction-and-chat-actions && mvn compile exec:java
cd examples/10-queues && mvn compile exec:java
cd examples/11-typebot-and-chatwoot && mvn compile exec:java
cd examples/quickstart && mvn compile exec:java
cd examples/worker-realtime && mvn compile exec:java
```

## Run from the examples root

Build all modules:

```bash
cd examples
mvn compile
```

Run a specific module without changing directories:

```bash
cd examples
mvn -pl 07-send-text-message compile exec:java
```

## Copy one example elsewhere

Each module only needs:

- its own `pom.xml`
- `src/main/java/*.java`

Update the parent block in `pom.xml` if you copy a single example outside this repository, or replace it with an explicit SDK dependency:

```xml
<dependency>
  <groupId>com.vzaps</groupId>
  <artifactId>vzaps-sdk</artifactId>
  <version>0.1.0</version>
</dependency>
```

## Modules

1. `01-auth-and-list-instances`
2. `02-create-instance`
3. `03-instance-subscription`
4. `04-session-and-pairing`
5. `05-configure-webhook`
6. `06-realtime-subscribe`
7. `07-send-text-message`
8. `08-send-media-and-interactive`
9. `09-send-poll-reaction-and-chat-actions`
10. `10-queues`
11. `11-typebot-and-chatwoot`
12. `quickstart`
13. `worker-realtime`

## Spring Boot

`spring-boot/` contains a configuration snippet only. It is not wired as a runnable Maven module yet.

## Coverage

- Auth and instance listing
- Instance creation and billing subscription checkout
- Session status, QR, and phone pairing code
- Webhook and realtime subscription
- Text, media, buttons, list, poll, reactions, presence
- Queue list/remove/purge examples
- TypeBot and Chatwoot integration examples
