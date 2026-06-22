# LucentClient

A Fabric-based Minecraft client mod built on the Lucent library.  
Provides various QoL improvements across graphics, HUD, performance, and utility categories.

---

## Modules

### 🎨 Graphics

| Module | Description |
|---|---|
| **Animations** | Customizes the first-person view model. Adjust swing speed, item position/rotation/scale, fire overlay height, shield height, and more. |
| **Block Overlay** | Replaces the default block selection box with a custom colored outline and/or fill. |
| **Death Animation** | Modifies the death animation behavior. |
| **Fullbright** | Overrides gamma to make the world fully bright. Adjustable brightness level. |
| **Hide Falling Block** | Hides falling block entities from rendering. |
| **Hurt Cam** | Toggles or adjusts the camera shake effect when taking damage. |
| **Particles** | Controls particle rendering behavior. |
| **Time Changer** | Locks the client-side sky to a fixed time (Day, Noon, Sunset, Night, Midnight, or a custom value). |

---

### 🖥️ HUD

| Module | Description |
|---|---|
| **Armor Status** | Displays your equipped armor durability on screen. |
| **Coordinates** | Shows X/Y/Z position, chunk-relative counter, facing direction, and current biome. Supports Vertical, Horizontal, and Simple layout modes. |
| **CPS** | Displays your current clicks per second. |
| **FPS** | Displays your current frames per second. |
| **Keystrokes** | Shows WASD keys and optionally mouse buttons and spacebar on screen. |
| **Ping** | Displays your current server ping. |
| **Potion Effects** | Shows active potion effects on the HUD. |
| **TPS** | Displays the server's ticks per second. |
| **Vanilla HUD** | Toggles or adjusts elements of the default Minecraft HUD. |

---

### ⚡ Performance

| Module | Description |
|---|---|
| **Entity Culling** | Skips rendering of entities that are not visible to the camera. |
| **HUD Culling** | Skips rendering of HUD elements when not needed. |
| **Memory Leak Fix** | Fixes known memory leaks: static biome ThreadLocal caching and crosshair target cleanup every tick. |
| **Network Fix** | Optimizes network pipeline: immutable passenger lists, fast UTF-8 encoding, fast VarInt serialization, legacy query buffer fix, and optimized frame decoding. |

---

### 🔧 Utility

| Module | Description |
|---|---|
| **Always Sprint** | Keeps the player sprinting automatically. |
| **Chatting** | Enhances the chat: custom background color, right-click to copy messages, and toggleable text shadow. |
| **Scrollable Tooltips** | Allows scrolling through tooltips that are taller than the screen. |
| **TNT Timer** | Renders the remaining fuse time above each primed TNT entity in the world. |
| **Zoom** | Zooms in while a keybind is held. Supports smooth zoom transition and scroll-to-adjust zoom level. |
