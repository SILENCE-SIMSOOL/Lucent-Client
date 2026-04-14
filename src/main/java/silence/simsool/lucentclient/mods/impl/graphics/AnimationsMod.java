package silence.simsool.lucentclient.mods.impl.graphics;

import java.awt.Color;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

@ModConfig.CategoryPriority(name = "General", priority = 1000)
@ModConfig.CategoryPriority(name = "Entity", priority = 900)
@ModConfig.CategoryPriority(name = "Item", priority = 800)
@ModConfig.CategoryPriority(name = "Overlay", priority = 700)
public class AnimationsMod extends Mod {

	public AnimationsMod() {
		super(
				"Animations", "Changes the appearance of the first-person view model",
				"Graphics",
				"equip, damage, hit, haste, camera, potion, particle, item, scale, swing, fire, shield",
				LucentClientUtils.getModIcon("animations")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(AnimationsMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "No Equip Reset",
		description = "",
		category = "General",
		priority = 1000
	)
	public static boolean NoEquipReset = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Custom Hit Color",
		description = "Enable custom colors for damage and armor hurt.",
		category = "Entity",
		priority = 910
	)
	public static boolean CustomHitColor = false;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Armor Hit Color",
		description = "",
		category = "Entity",
		parent = "CustomHitColor",
		priority = 905
	)
	public static boolean ArmorHitColor = false;

	@ModConfig(
		type = ConfigType.COLOR,
		name = "Damage Color",
		description = "Color of the entity when they take damage.",
		category = "Entity",
		parent = "UseDamageColor",
		priority = 900
	)
	public static Color HitColor = new Color(255, 0, 0, 76);

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "Swing Speed",
		description = "Speed of the player's arm swing animation. Higher values are faster. (default: 12)",
		category = "General",
		min = 1,
		max = 16,
		step = 1,
		priority = 990
	)
	public static int SwingSpeed = 8;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Ignore Haste",
		description = "Ignores the actual speed increase from Haste for the swing animation.",
		category = "General",
		priority = 985
	)
	public static boolean IgnoreHaste = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Disable Camera Potion Particles",
		description = "Hides potion particles from your perspective.",
		category = "General",
		priority = 980
	)
	public static boolean DisableCameraPotionParticles = true;

//	@ModConfig(
//		type = ConfigType.SWITCH,
//		name = "Flat Item",
//		description = "Renders held items as flat 2D maps.",
//		category = "Item",
//		priority = 800
//	)
//	public static boolean FlatItem = false;

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "Item Scale",
		description = "Scale factor for your held item.",
		category = "Item",
		min = 0.1,
		max = 2.0,
		step = 0.05,
		priority = 790
	)
	public static double ItemScale = 1.0;

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "Item X",
		description = "X offset for your held item.",
		category = "Item",
		min = -2.0,
		max = 2.0,
		step = 0.05,
		priority = 780
	)
	public static double HeldItemX = 0.0;

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "Item Y",
		description = "Y offset for your held item.",
		category = "Item",
		min = -2.0,
		max = 2.0,
		step = 0.05,
		priority = 770
	)
	public static double HeldItemY = 0.0;

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "Item Z",
		description = "Z offset for your held item.",
		category = "Item",
		min = -2.0,
		max = 2.0,
		step = 0.05,
		priority = 760
	)
	public static double HeldItemZ = 0.0;

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "Item Yaw",
		description = "Yaw rotation for your held item.",
		category = "Item",
		min = -180.0,
		max = 180.0,
		step = 1.0,
		priority = 750
	)
	public static double HeldItemYaw = 0.0;

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "Item Pitch",
		description = "Pitch rotation for your held item.",
		category = "Item",
		min = -180.0,
		max = 180.0,
		step = 1.0,
		priority = 740
	)
	public static double HeldItemPitch = 0.0;

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "Item Roll",
		description = "Roll rotation for your held item.",
		category = "Item",
		min = -180.0,
		max = 180.0,
		step = 1.0,
		priority = 730
	)
	public static double HeldItemRoll = 0.0;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Fire Overlay",
		description = "Toggle the visibility of the fire overlay on your screen.",
		category = "Overlay",
		priority = 700
	)
	public static boolean FireOverlay = true;

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "Fire Height",
		description = "Adjust the height offset of the fire overlay.",
		category = "Overlay",
		parent = "FireOverlay",
		min = 0.0,
		max = 1.0,
		step = 0.01,
		priority = 690
	)
	public static double FireHeight = 0.5;

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "Shield Height",
		description = "Y offset for your shield.",
		category = "Shield",
		min = -0.5,
		max = 0.5,
		step = 0.1
	)
	public static float ShieldHeight = 0.0f;

}