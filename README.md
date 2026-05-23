# ATM10 Router — Velocity Proxy Plugin

Routes Minecraft clients connecting to `mc.lexylou.net` to the correct backend server based on protocol version.

## Routing Table

| Protocol | Version | Modloader | Backend |
|----------|---------|-----------|---------|
| 767 | 1.21.1 | NeoForge | `atm10` (alfie:25575) |
| 763 | 1.20.1 | Forge | `liminal` (alfie:25577) |
| All others | Latest | Vanilla/Bedrock | `vanilla` (localhost:25575) |

## Build

```bash
javac -cp /opt/minecraft/velocity/velocity.jar -d out \
  src/main/java/com/lexylou/velocity/Atm10Router.java
cp src/main/resources/velocity-plugin.json out/
cd out && jar cf atm10-router.jar .
```

## Deploy

```bash
sudo cp atm10-router.jar /opt/minecraft/velocity/plugins/
sudo systemctl restart velocity
```

## How It Works

- `PlayerChooseInitialServerEvent` fires when a client connects
- Reads `player.getProtocolVersion().getProtocol()` to detect the client version
- Routes 767 → ATM10, 763 → Liminal, everything else → vanilla
- Protocol versions are mutually exclusive across the three servers, so no ambiguous routing
