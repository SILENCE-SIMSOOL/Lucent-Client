package silence.simsool.lucentclient.account;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import silence.simsool.lucent.ui.utils.nvg.Fonts;
import silence.simsool.lucent.ui.utils.nvg.Image;
import silence.simsool.lucent.ui.utils.nvg.NVGRenderer;

public class AccountAvatarManager {

	private static final Map<UUID, Image> AVATAR_CACHE = new ConcurrentHashMap<>();
	private static final Map<UUID, Boolean> LOADING_SET = new ConcurrentHashMap<>();
	private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
		.connectTimeout(Duration.ofSeconds(5))
		.build();

	public static Image getAvatar(UUID uuid) {
		if (uuid == null) return null;
		Image cached = AVATAR_CACHE.get(uuid); if (cached != null) return cached;

		if (!LOADING_SET.containsKey(uuid)) {
			LOADING_SET.put(uuid, true);
			CompletableFuture.runAsync(() -> {
				try {
					String url = "https://minotar.net/helm/" + uuid.toString().replace("-", "") + "/64.png";
					HttpRequest request = HttpRequest.newBuilder()
						.uri(URI.create(url))
						.timeout(Duration.ofSeconds(6))
						.GET()
						.build();

					HttpResponse<byte[]> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofByteArray());
					if (response.statusCode() == 200 && response.body() != null && response.body().length > 0) {
						byte[] bytes = response.body();
						ByteBuffer buf = ByteBuffer.allocateDirect(bytes.length);
						buf.put(bytes);
						buf.flip();
						Image img = new Image("avatar_" + uuid, false, new ByteArrayInputStream(bytes), buf);
						AVATAR_CACHE.put(uuid, img);
					}
				} catch (Exception ignored) {
				} finally {
					LOADING_SET.remove(uuid);
				}
			});
		}

		return null;
	}

	public static void renderAvatar(UUID uuid, String name, float x, float y, float size, float radius) {
		Image avatar = getAvatar(uuid);
		if (avatar != null) {
			try {
				NVGRenderer.image(avatar, x, y, size, size, radius);
				return;
			} catch (Exception ignored) {}
		}

		// Fallback placeholder
		NVGRenderer.rect(x, y, size, size, 0xFF2A2E3D, radius);
		NVGRenderer.outlineRect(x, y, size, size, 1.0f, 0x33FFFFFF, radius);
		if (name != null && !name.isEmpty()) {
			String initial = name.substring(0, 1).toUpperCase();
			NVGRenderer.centerText(initial, x + size / 2.0f, y + size / 2.0f - 5.0f, Fonts.PRETENDARD_BOLD, 0xFFFFFFFF, size * 0.55f);
		}
	}

}