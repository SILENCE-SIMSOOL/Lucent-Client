package silence.simsool.lucentclient.mods.impl.graphics;

import java.awt.Color;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;

@ModConfig.CategoryPriority(name = "General", priority = 1000)
@ModConfig.CategoryPriority(name = "Entity", priority = 900)
@ModConfig.CategoryPriority(name = "Item", priority = 800)
@ModConfig.CategoryPriority(name = "Overlay", priority = 700)
public class AnimationsMod extends Mod {

	public AnimationsMod() {
		super("Animations", "Adjusts various animations and visual effects.", "Graphics", "animation, render, visual", "lucid:render");
	}

	public static boolean isEnabled() {
		return Lucent.config.getModule(AnimationsMod.class).isEnabled;
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
		name = "No Hurt Cam",
		description = "Disables camera shaking when taking damage.",
		category = "General",
		priority = 950
	)
	public static boolean NoHurtCam = true;

	@ModConfig(
		type = ConfigType.COLOR,
		name = "Damage Color",
		description = "Color of the entity when they take damage.",
		category = "Entity",
		priority = 900
	)
	public static Color DamageColor = new Color(255, 0, 0, 76);

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "Swing Speed",
		description = "Speed of the player's arm swing animation.",
		category = "General",
		min = 0.1,
		max = 5.0,
		step = 0.1,
		priority = 990
	)
	public static double SwingSpeed = 1.0;

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

	@ModConfig(
		type = ConfigType.COLOR,
		name = "Armor Hurt Color",
		description = "Color of the armor when the entity takes damage.",
		category = "Entity",
		priority = 890
	)
	public static Color ArmorHurtColor = new Color(255, 0, 0, 76);

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Flat Item",
		description = "Renders held items as flat 2D maps.",
		category = "Item",
		priority = 800
	)
	public static boolean FlatItem = false;

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
		parent = "fireOverlay",
		min = 0.0,
		max = 1.0,
		step = 0.01,
		priority = 690
	)
	public static double FireHeight = 0.5;

}