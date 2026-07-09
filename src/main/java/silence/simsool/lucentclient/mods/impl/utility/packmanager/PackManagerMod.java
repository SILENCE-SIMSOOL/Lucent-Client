package silence.simsool.lucentclient.mods.impl.utility.packmanager;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucent.general.utils.LucentCategory;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class PackManagerMod extends Mod {

	private static final Pattern serverPackIdPattern = Pattern.compile("server/[0-9A-F]{8}/([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})");
	private static final Set<String> hypixelPackIds = ConcurrentHashMap.newKeySet();

	public PackManagerMod() {
		super(
				"lucent.config.lucentclient.packmanagermod.general.name",
				"lucent.config.lucentclient.packmanagermod.general.description",
				LucentCategory.UTILITY,
				"resource, pack, manager, server, unlock",
				LucentClientUtils.getModIcon("packmanager")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(PackManagerMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.packmanagermod.property.serverunlocker.name",
		description = "lucent.config.lucentclient.packmanagermod.property.serverunlocker.description"
	)
	public static boolean ServerUnlocker = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.packmanagermod.property.disablepackoverride.name",
		description = "lucent.config.lucentclient.packmanagermod.property.disablepackoverride.description"
	)
	public static boolean DisablePackOverride = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.packmanagermod.property.blockpackdownload.name",
		description = "lucent.config.lucentclient.packmanagermod.property.blockpackdownload.description"
	)
	public static boolean BlockPackDownload = true;

	public static void addPack(UUID id) {
		hypixelPackIds.add(id.toString());
	}

	public static boolean removePack(UUID id) {
		if (id != null) return hypixelPackIds.remove(id.toString());
		boolean hasElements = !hypixelPackIds.isEmpty();
		clearPacks();
		return hasElements;
	}

	public static void clearPacks() {
		hypixelPackIds.clear();
	}

	public static boolean fromHypixelPack(String packId) {
		Matcher matcher = serverPackIdPattern.matcher(packId);
		if (!matcher.matches()) return false;
		String id = matcher.group(1);
		return hypixelPackIds.contains(id);
	}

}