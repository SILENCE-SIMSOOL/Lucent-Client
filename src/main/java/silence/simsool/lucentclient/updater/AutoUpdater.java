package silence.simsool.lucentclient.updater;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.fabricmc.loader.api.FabricLoader;
import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.utils.useful.UChat;
import silence.simsool.lucentclient.LucentClient;
import silence.simsool.lucentclient.mods.impl.utility.AutoUpdateMod;

public class AutoUpdater {

	private static final Pattern FILENAME_PATTERN = Pattern.compile(
		"^([A-Za-z0-9_.-]+)-([0-9]+(?:\\.[0-9]+)+)-mc([A-Za-z0-9_.-]+)\\.jar$",
		Pattern.CASE_INSENSITIVE
	);

	public static void cleanOldJars() {
		try {
			Path modsDir = FabricLoader.getInstance().getGameDir().resolve("mods");
			if (!Files.exists(modsDir)) return;

			String currentVer = LucentClient.VERSION;
			String currentMc = getMinecraftVersion();

			File[] files = modsDir.toFile().listFiles((dir, name) -> name.toLowerCase(Locale.ROOT).startsWith("lucentclient") && name.endsWith(".jar"));
			if (files == null) return;

			for (File file : files) {
				String fileName = file.getName();
				Matcher matcher = FILENAME_PATTERN.matcher(fileName);
				if (matcher.matches()) {
					String fileModVer = matcher.group(2);
					String fileMcVer = matcher.group(3);

					if (isMcVersionMatching(fileMcVer, currentMc)) {
						if (!fileModVer.equalsIgnoreCase(currentVer) && isVersionNewer(currentVer, fileModVer)) {
							Lucent.LOG.info("Cleaning up old LucentClient jar: " + fileName);
							try {
								Files.deleteIfExists(file.toPath());
							} catch (Exception e) {
								file.deleteOnExit();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			Lucent.LOG.error("Failed to clean old LucentClient jars: " + e.getMessage());
		}
	}

	public static void checkAndUpdate() {
		if (!AutoUpdateMod.autoUpdate) {
			Lucent.LOG.info("LucentClient Auto update is disabled.");
			return;
		}

		CompletableFuture.runAsync(() -> {
			try {
				cleanOldJars();

				String mcVer = getMinecraftVersion();
				Lucent.LOG.info("Checking updates for LucentClient (Minecraft version: " + mcVer + ", Current: " + LucentClient.VERSION + ")");

				String releasesUrl = "https://api.github.com/repos/SILENCE-SIMSOOL/Lucent-Client/releases";
				URL url = URI.create(releasesUrl).toURL();
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("User-Agent", "LucentClient-Updater");
				conn.setConnectTimeout(5000);
				conn.setReadTimeout(5000);

				if (conn.getResponseCode() != 200) {
					Lucent.LOG.warn("Failed to check release API. Response code: " + conn.getResponseCode());
					return;
				}

				String json;
				try (InputStream in = conn.getInputStream()) {
					json = new String(in.readAllBytes());
				}

				AssetInfo bestAsset = findBestAsset(json, "LucentClient", LucentClient.VERSION, mcVer);
				if (bestAsset != null) {
					Lucent.LOG.info("Found newer version: " + bestAsset.version + " (URL: " + bestAsset.downloadUrl + ")");
					downloadAndApply(bestAsset);
				} else {
					Lucent.LOG.info("LucentClient is up to date.");
				}
			} catch (Exception e) {
				Lucent.LOG.error("Error while checking auto update: " + e.getMessage());
			}
		});
	}

	private static AssetInfo findBestAsset(String json, String targetModName, String currentVersion, String currentMcVersion) {
		AssetInfo best = null;
		String highestVer = currentVersion;

		int assetIdx = 0;
		while ((assetIdx = json.indexOf("\"browser_download_url\":", assetIdx)) != -1) {
			int urlStart = json.indexOf("\"", assetIdx + 23) + 1;
			int urlEnd = json.indexOf("\"", urlStart);
			String downloadUrl = json.substring(urlStart, urlEnd);

			int nameIdx = json.lastIndexOf("\"name\":", assetIdx);
			if (nameIdx != -1) {
				int nameStart = json.indexOf("\"", nameIdx + 7) + 1;
				int nameEnd = json.indexOf("\"", nameStart);
				String assetName = json.substring(nameStart, nameEnd);

				String lowerName = assetName.toLowerCase(Locale.ROOT);
				if (lowerName.contains("-library") || lowerName.contains("-lite")) {
					assetIdx = urlEnd;
					continue;
				}

				Matcher matcher = FILENAME_PATTERN.matcher(assetName);
				if (matcher.matches()) {
					String modName = matcher.group(1);
					String modVer = matcher.group(2);
					String mcVer = matcher.group(3);

					if ((modName.equalsIgnoreCase(targetModName) || modName.equalsIgnoreCase("Lucent-Client")) && isMcVersionMatching(mcVer, currentMcVersion)) {
						if (isVersionNewer(modVer, highestVer)) {
							highestVer = modVer;
							best = new AssetInfo(assetName, modVer, downloadUrl);
						}
					}
				}
			}
			assetIdx = urlEnd;
		}

		return best;
	}

	public static volatile boolean isDownloading = false;
	public static volatile int progress = 0;
	public static volatile String statusText = "";

	private static void downloadAndApply(AssetInfo asset) {
		try {
			Path modsDir = FabricLoader.getInstance().getGameDir().resolve("mods");
			if (!Files.exists(modsDir)) {
				Files.createDirectories(modsDir);
			}

			Path targetFile = modsDir.resolve(asset.assetName);
			Path tempFile = modsDir.resolve(asset.assetName + ".tmp");

			isDownloading = true;
			statusText = "Downloading Lucent Client v" + asset.version + "...";
			progress = 0;

			UChat.chat(LucentClient.PREFIX + " §aNew update found: §eLucent Client v" + asset.version + "§a. Downloading...");

			URL url = URI.create(asset.downloadUrl).toURL();
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("User-Agent", "LucentClient-Updater");
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(30000);

			long totalBytes = conn.getContentLengthLong();
			long downloadedBytes = 0;

			try (InputStream in = new BufferedInputStream(conn.getInputStream());
				 FileOutputStream out = new FileOutputStream(tempFile.toFile())) {

				byte[] buffer = new byte[8192];
				int bytesRead;
				int lastLoggedProgress = -1;
				while ((bytesRead = in.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
					downloadedBytes += bytesRead;
					if (totalBytes > 0) {
						progress = (int) ((downloadedBytes * 100) / totalBytes);
						if (progress % 25 == 0 && progress != lastLoggedProgress) {
							lastLoggedProgress = progress;
							Lucent.LOG.info("Downloading update: " + progress + "%");
						}
					}
				}
			}

			Files.move(tempFile, targetFile, StandardCopyOption.REPLACE_EXISTING);

			File currentJar = getCurrentModJar();
			if (currentJar != null && currentJar.exists() && !currentJar.getName().equals(asset.assetName)) {
				currentJar.deleteOnExit();
			}

			isDownloading = false;
			statusText = "Download complete!";
			progress = 100;

			UChat.chat(LucentClient.PREFIX + " §aUpdate completed! §e(" + asset.assetName + ") §fPlease restart Minecraft to apply.");
			Lucent.LOG.info("Downloaded updated LucentClient jar: " + targetFile.getFileName());
		} catch (Exception e) {
			isDownloading = false;
			statusText = "Download failed!";
			Lucent.LOG.error("Failed to download update: " + e.getMessage());
			UChat.chat(LucentClient.PREFIX + " §cFailed to download update: " + e.getMessage());
		}
	}

	private static File getCurrentModJar() {
		try {
			return new File(AutoUpdater.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		} catch (Exception e) {
			return null;
		}
	}

	public static String getMinecraftVersion() {
		return FabricLoader.getInstance()
			.getModContainer("minecraft")
			.map(c -> c.getMetadata().getVersion().getFriendlyString())
			.orElse("26.2");
	}

	public static boolean isMcVersionMatching(String assetMcVersion, String currentMcVersion) {
		if (assetMcVersion == null || currentMcVersion == null) return false;
		String cleanAsset = assetMcVersion.toLowerCase(Locale.ROOT).replace("mc", "").trim();
		String cleanCurrent = currentMcVersion.toLowerCase(Locale.ROOT).replace("mc", "").trim();
		return cleanAsset.equals(cleanCurrent) || cleanAsset.startsWith(cleanCurrent) || cleanCurrent.startsWith(cleanAsset);
	}

	public static boolean isVersionNewer(String candidateVersion, String currentVersion) {
		try {
			String[] cParts = candidateVersion.split("\\.");
			String[] curParts = currentVersion.split("\\.");
			int length = Math.max(cParts.length, curParts.length);
			for (int i = 0; i < length; i++) {
				int cVal = i < cParts.length ? Integer.parseInt(cParts[i].replaceAll("[^0-9]", "")) : 0;
				int curVal = i < curParts.length ? Integer.parseInt(curParts[i].replaceAll("[^0-9]", "")) : 0;
				if (cVal > curVal) return true;
				if (cVal < curVal) return false;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	private static class AssetInfo {
		final String assetName;
		final String version;
		final String downloadUrl;

		AssetInfo(String assetName, String version, String downloadUrl) {
			this.assetName = assetName;
			this.version = version;
			this.downloadUrl = downloadUrl;
		}
	}

}