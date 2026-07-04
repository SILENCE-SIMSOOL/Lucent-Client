<h3 align="center">
	<img src="https://raw.githubusercontent.com/SILENCE-SIMSOOL/Lucent-Client/main/docs/icon.png" alt="Lucent Icon" width="128" height="128" />
</h3>

<h1 align="center">Lucent Client</h1>

<p align="center">
	A Fabric-based Minecraft client mod built on the Lucent library.  
    Provides various QoL improvements across graphics, HUD, performance, and utility categories.
</p>

<p align="center">
	<a href="https://github.com/SILENCE-SIMSOOL/Lucent-Client/releases" target="_blank">
		<img alt="release" src="https://img.shields.io/github/v/release/SILENCE-SIMSOOL/Lucent-Client?color=E0E0E0&style=flat-square" />
	</a>
	<a href="https://github.com/SILENCE-SIMSOOL/Lucent-Client/releases" target="_blank">
		<img alt="downloads" src="https://img.shields.io/github/downloads/SILENCE-SIMSOOL/Lucent-Client/total?color=8b4db6&style=flat-square" />
	</a>
	<a href="https://discord.gg/2Zt8HDksJs" target="_blank">
		<img alt="discord" src="https://img.shields.io/discord/1312525891225784421?color=5865F2&label=discord&style=flat-square" />
	</a>
</p>

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
| **Better F5** | Disables the front-facing third-person view, allowing you to quickly toggle between first-person and back-facing third-person view. |
| **Chatting** | Enhances the chat: custom background color, right-click to copy messages, and toggleable text shadow. |
| **Scrollable Tooltips** | Allows scrolling through tooltips that are taller than the screen. |
| **TNT Timer** | Renders the remaining fuse time above each primed TNT entity in the world. |
| **Zoom** | Zooms in while a keybind is held. Supports smooth zoom transition and scroll-to-adjust zoom level. |

---

## Credits

- [FerriteCore](https://github.com/malte0811/FerriteCore)
- [Krypton](https://github.com/astei/krypton)

