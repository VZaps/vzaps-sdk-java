# VZaps Java SDK Examples

Runnable Maven projects that consume the published SDK from Maven Central (`com.vzaps:vzaps-sdk`).

Each numbered folder is a **standalone Maven project** with its own `pom.xml`. You do **not** need to clone the full SDK repository to run one example.

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

## Option A — one example folder (recommended)

Download only one example folder, for example [`07-send-text-message`](https://github.com/VZaps/vzaps-sdk-java/tree/main/examples/07-send-text-message):

1. Open the folder on GitHub and choose **Download ZIP**, or run:

```bash
npx --yes degit VZaps/vzaps-sdk-java/examples/07-send-text-message vzaps-java-send-text
cd vzaps-java-send-text
```

2. Run:

```bash
mvn compile exec:java
```

Each copied folder only needs:

- its own `pom.xml`
- `src/main/java/*.java`

The `pom.xml` already declares `com.vzaps:vzaps-sdk:0.1.0` explicitly. No parent project is required.

## Option B — sparse checkout

```bash
git clone --depth 1 --filter=blob:none --sparse https://github.com/VZaps/vzaps-sdk-java.git
cd vzaps-sdk-java
git sparse-checkout set examples/07-send-text-message
cd examples/07-send-text-message
mvn compile exec:java
```

## Option C — full repository clone

Use this when you want every example locally or you are contributing to the SDK:

```bash
git clone https://github.com/VZaps/vzaps-sdk-java.git
cd vzaps-sdk-java/examples/07-send-text-message
mvn compile exec:java
```

Build all modules from the examples root:

```bash
cd examples
mvn compile
mvn -pl 07-send-text-message compile exec:java
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
