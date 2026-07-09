package silence.simsool.lucentclient.mods.impl.utility;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class DynamicItemModels {

	private static final Identifier diamondSword = getIdentifier(Items.DIAMOND_SWORD);
	private static final Identifier goldenSword = getIdentifier(Items.GOLDEN_SWORD);

	private static final Map<String, AttuneInfo> attunedModels = new HashMap<>();
	private static final Set<String> katanas = new HashSet<>();
	private static final Set<String> fungiCutters = new HashSet<>();

	static {
		attunedModels.put("FIREDUST_DAGGER", new AttuneInfo(1, goldenSword));
		attunedModels.put("BURSTFIRE_DAGGER", new AttuneInfo(1, goldenSword));
		attunedModels.put("HEARTFIRE_DAGGER", new AttuneInfo(1, goldenSword));
		attunedModels.put("MAWDUST_DAGGER", new AttuneInfo(3, diamondSword));
		attunedModels.put("BURSTMAW_DAGGER", new AttuneInfo(3, diamondSword));
		attunedModels.put("HEARTMAW_DAGGER", new AttuneInfo(3, diamondSword));

		katanas.add("VOIDEDGE_KATANA");
		katanas.add("VORPAL_KATANA");
		katanas.add("ATOMSPLIT_KATANA");

		fungiCutters.add("FUNGI_CUTTER");
		fungiCutters.add("FUNGI_CUTTER_2");
		fungiCutters.add("FUNGI_CUTTER_3");
	}

	public static Identifier resolve(String skyblockId, ItemStack stack, CompoundTag customData, Identifier fallback) {
		if (attunedModels.containsKey(skyblockId)) {
			AttuneInfo info = attunedModels.get(skyblockId);
			int attuneMode = customData.contains("td_attune_mode") ? customData.getInt("td_attune_mode").orElse(-1) : -1;
			if (attuneMode == info.mode) {
				return info.model;
			}
			return fallback;
		}

		if (katanas.contains(skyblockId)) {
			if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.getCooldowns().isOnCooldown(stack)) {
				return goldenSword;
			}
			return fallback;
		}

		if (fungiCutters.contains(skyblockId)) {
			String mode = customData.contains("fungi_cutter_mode") ? customData.getString("fungi_cutter_mode").orElse(null) : null;
			if ("RED".equals(mode)) {
				return getIdentifier(Items.RED_MUSHROOM);
			} else if ("BROWN".equals(mode)) {
				return getIdentifier(Items.BROWN_MUSHROOM);
			}
			return fallback;
		}

		return fallback;
	}

	private static Identifier getIdentifier(Item item) {
		return item.components().get(DataComponents.ITEM_MODEL);
	}

	private static class AttuneInfo {
		final int mode;
		final Identifier model;

		AttuneInfo(int mode, Identifier model) {
			this.mode = mode;
			this.model = model;
		}
	}
}
