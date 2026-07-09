package silence.simsool.lucentclient.mods.impl.utility.packmanager.utils;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class PackManagerUtils {

	public static CompoundTag getCustomData(ItemStack stack) {
		CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
		return customData.copyTag();
	}

	public static String getSkyblockId(ItemStack stack) {
		return getSkyblockId(getCustomData(stack));
	}

	public static String getSkyblockSkinId(ItemStack stack) {
		return getNormalizedId(getCustomData(stack), "skin");
	}

	public static String getNormalizedId(CompoundTag tag, String key) {
		if (tag == null || !tag.contains(key)) return null;
		String value = tag.getString(key).orElse(null);
		if (value == null || value.isEmpty()) return null;
		return value.replace(":", "-");
	}

	public static String getSkyblockId(CompoundTag tag) {
		return getNormalizedId(tag, "id");
	}
}
