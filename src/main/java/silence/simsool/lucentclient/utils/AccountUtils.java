package silence.simsool.lucentclient.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

import silence.simsool.lucent.general.utils.OSUtils;

/**
 * This class accesses the user's HWID, which may cause false positives
 * in some antivirus or RAT detection systems.
 *
 * Account Switch stores account data locally. If this file is stolen,
 * the stored account information could otherwise be exposed or reused.
 *
 * To reduce this risk, Lucent derives a unique encryption key for each user
 * and uses it to encrypt the stored account data. This prevents a stolen
 * account file from being directly used on another system.
 *
 * We believe this simple protection can help keep users' accounts safer
 * in many real-world situations.
 */
public class AccountUtils {

	public static String getHWID() {
		try {
			switch (OSUtils.getOS()) {
				case WINDOWS:
					return getWindowsMachineGuid();
				case LINUX:
					return getLinuxMachineId();
				case MAC:
					return getMacPlatformUuid();
				default:
					return getDefaultHwid();
			}
		} catch (Exception ignored) {
			return getDefaultHwid();
		}
	}

	private static String getWindowsMachineGuid() throws IOException {
		Process process = new ProcessBuilder(
			"reg",
			"query",
			"HKLM\\SOFTWARE\\Microsoft\\Cryptography",
			"/v",
			"MachineGuid"
		).redirectErrorStream(true).start();

		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(process.getInputStream()))) {

			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();

				if (line.startsWith("MachineGuid")) {
					String[] parts = line.split("\\s+");
					return parts[parts.length - 1];
				}
			}
		}

		return getDefaultHwid();
	}

	private static String getLinuxMachineId() throws IOException {
		Path path = Path.of("/etc/machine-id");

		if (!Files.exists(path)) {
			path = Path.of("/var/lib/dbus/machine-id");
		}

		if (!Files.exists(path)) return getDefaultHwid();

		return Files.readString(path).trim();
	}

	private static String getMacPlatformUuid() throws IOException {
		Process process = new ProcessBuilder(
			"ioreg",
			"-rd1",
			"-c",
			"IOPlatformExpertDevice"
		).redirectErrorStream(true).start();

		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(process.getInputStream()))) {

			String line;
			while ((line = reader.readLine()) != null) {
				if (line.contains("IOPlatformUUID")) {
					int firstQuote = line.indexOf('"', line.indexOf('=') + 1);
					int lastQuote = line.lastIndexOf('"');

					if (firstQuote != -1 && lastQuote > firstQuote) {
						return line.substring(firstQuote + 1, lastQuote);
					}
				}
			}
		}

		return getDefaultHwid();
	}

	private static String getDefaultHwid() {
		char[] data = {
			0x16, 0x0F, 0x19, 0x1F, 0x14, 0x0E, 0x19, 0x16,
			0x13, 0x1F, 0x14, 0x0E, 0x77, 0x1E, 0x1F, 0x1C,
			0x1B, 0x0F, 0x16, 0x0E, 0x77, 0x12, 0x0D, 0x13,
			0x1E
		};

		for (int i = 0; i < data.length; i++) {
			data[i] ^= 0x5A;
		}

		return new String(data);
	}

}