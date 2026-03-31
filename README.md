# Ethical Auto Totem (Fabric)

Client-side Fabric mod that listens for your own totem pop packet. When it triggers, the mod:

1. Opens your inventory immediately.
2. Waits 16 ticks (about 0.8 seconds).
3. Picks up a totem from inventory and places it into offhand.

## Notes

- Minecraft: **1.21.x**
- Loader: **Fabric**
- Java: **21+**
- Requires `fabric-api`.
- This is client-side logic and does not alter server code.
