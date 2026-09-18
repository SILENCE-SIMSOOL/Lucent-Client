package silence.simsool.lucentclient.account;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import silence.simsool.lucent.general.utils.OSUtils;

public class AccountStorage {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final int GCM_IV_LENGTH = 12;
	private static final int GCM_TAG_LENGTH = 128;
	private static SecretKey cachedKey = null;

	public static class StorageData {
		public UUID activeAccountId;
		public List<Account> accounts = new ArrayList<>();
	}

	public static StorageData load() {
		File file = getStorageFile();
		if (!file.exists() || file.length() == 0) {
			return new StorageData();
		}

		try {
			byte[] fileBytes = Files.readAllBytes(file.toPath());
			if (fileBytes.length <= GCM_IV_LENGTH) {
				return new StorageData();
			}

			SecretKey key = getOrDeriveKey();
			byte[] iv = new byte[GCM_IV_LENGTH];
			System.arraycopy(fileBytes, 0, iv, 0, GCM_IV_LENGTH);

			byte[] cipherText = new byte[fileBytes.length - GCM_IV_LENGTH];
			System.arraycopy(fileBytes, GCM_IV_LENGTH, cipherText, 0, cipherText.length);

			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
			GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
			cipher.init(Cipher.DECRYPT_MODE, key, spec);

			byte[] plainBytes = cipher.doFinal(cipherText);
			String json = new String(plainBytes, StandardCharsets.UTF_8);

			StorageData data = GSON.fromJson(json, StorageData.class);
			return data != null ? data : new StorageData();
		} catch (Exception e) {
			return new StorageData();
		}
	}

	public static boolean save(StorageData data) {
		try {
			File file = getStorageFile();
			File dir = file.getParentFile();
			if (dir != null && !dir.exists()) {
				dir.mkdirs();
			}

			String json = GSON.toJson(data);
			byte[] plainBytes = json.getBytes(StandardCharsets.UTF_8);

			SecretKey key = getOrDeriveKey();
			byte[] iv = new byte[GCM_IV_LENGTH];
			new SecureRandom().nextBytes(iv);

			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
			GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
			cipher.init(Cipher.ENCRYPT_MODE, key, spec);

			byte[] cipherText = cipher.doFinal(plainBytes);

			byte[] result = new byte[GCM_IV_LENGTH + cipherText.length];
			System.arraycopy(iv, 0, result, 0, GCM_IV_LENGTH);
			System.arraycopy(cipherText, 0, result, GCM_IV_LENGTH, cipherText.length);

			File tempFile = new File(dir, "accounts.dat.tmp");
			Files.write(tempFile.toPath(), result);
			Files.move(tempFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static File getStorageFile() {
		return new File(OSUtils.getLucentDir(), "accounts.dat");
	}

	private static synchronized SecretKey getOrDeriveKey() {
		if (cachedKey != null) {
			return cachedKey;
		}

		String hwidBase64 = getHwidBase64();
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] keyBytes = digest.digest(hwidBase64.getBytes(StandardCharsets.UTF_8));
			cachedKey = new SecretKeySpec(keyBytes, "AES");
			return cachedKey;
		} catch (Exception e) {
			byte[] fallback = new byte[32];
			cachedKey = new SecretKeySpec(fallback, "AES");
			return cachedKey;
		}
	}

	public static String getHwidBase64() {
		String cpuId = "";
		String mbUuid = "";

		try {
			Process process = new ProcessBuilder(
				"powershell", "-NoProfile", "-Command",
				"$c = (Get-CimInstance Win32_Processor).ProcessorId; $m = (Get-CimInstance Win32_ComputerSystemProduct).UUID; \"$c|$m\""
			).redirectErrorStream(true).start();

			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
				String line;
				while ((line = reader.readLine()) != null) {
					line = line.trim();
					if (line.contains("|")) {
						String[] parts = line.split("\\|", 2);
						cpuId = parts[0].trim();
						if (parts.length > 1) {
							mbUuid = parts[1].trim();
						}
						break;
					}
				}
			}
			process.waitFor(3, TimeUnit.SECONDS);
		} catch (Exception ignored) {
		}

		if (cpuId.isEmpty() && mbUuid.isEmpty()) {
			cpuId = System.getenv("PROCESSOR_IDENTIFIER") != null ? System.getenv("PROCESSOR_IDENTIFIER") : "GENERIC_CPU";
			mbUuid = System.getProperty("user.name", "GENERIC_USER");
		}

		String combined = cpuId + "#" + mbUuid;
		return Base64.getEncoder().encodeToString(combined.getBytes(StandardCharsets.UTF_8));
	}

}