package silence.simsool.lucentclient.mods.impl.graphics;

import silence.simsool.lucent.general.utils.LucentCategory;

import java.awt.Color;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.Align;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfigExtra;
import silence.simsool.lucentclient.utils.LucentClientUtils;

@ModConfig.CategoryPriority(name = "Entity", priority = 900)
@ModConfig.CategoryPriority(name = "Item", priority = 800)
@ModConfig.CategoryPriority(name = "Overlay", priority = 700)
public class AnimationsMod extends Mod {

	public AnimationsMod() {
		super(
				"lucent.config.lucentclient.animationsmod.general.name", "lucent.config.lucentclient.animationsmod.general.description",
				LucentCategory.GRAPHICS,
				"animation, equip, damage, hit, haste, camera, potion, particle, item, scale, swing, fire, shield",
				LucentClientUtils.getModIcon("animations")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(AnimationsMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.animationsmod.property.noequipreset.name",
		description = "lucent.config.lucentclient.animationsmod.property.noequipreset.description",
		priority = 1000
	)
	public static boolean NoEquipReset = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.animationsmod.property.fixslotdrop.name",
		description = "lucent.config.lucentclient.animationsmod.property.fixslotdrop.description",
		priority = 990
	)
	public static boolean FixSlotDrop = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.animationsmod.property.customhitcolor.name",
		description = "lucent.config.lucentclient.animationsmod.property.customhitcolor.description",
		category = "Entity",
		priority = 910
	)
	public static boolean CustomHitColor = false;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.animationsmod.property.armorhitcolor.name",
		description = "lucent.config.lucentclient.animationsmod.property.armorhitcolor.description",
		category = "Entity",
		parent = "CustomHitColor",
		priority = 905
	)
	public static boolean ArmorHitColor = false;

	@ModConfig(
		type = ConfigType.COLOR,
		name = "lucent.config.lucentclient.animationsmod.property.hitcolor.name",
		description = "lucent.config.lucentclient.animationsmod.property.hitcolor.description",
		category = "Entity",
		parent = "CustomHitColor",
		priority = 900
	)
	public static Color HitColor = new Color(255, 0, 0, 76);

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "lucent.config.lucentclient.animationsmod.property.swingspeed.name",
		description = "lucent.config.lucentclient.animationsmod.property.swingspeed.description",
		min = 1,
		max = 16,
		step = 1,
		priority = 990
	)
	public static int SwingSpeed = 8;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.animationsmod.property.ignorehaste.name",
		description = "lucent.config.lucentclient.animationsmod.property.ignorehaste.description",
		priority = 985
	)
	public static boolean IgnoreHaste = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.animationsmod.property.disablecamerapotionparticles.name",
		description = "lucent.config.lucentclient.animationsmod.property.disablecamerapotionparticles.description",
		priority = 980
	)
	public static boolean DisableCameraPotionParticles = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.animationsmod.property.disableuseanimation.name",
		description = "lucent.config.lucentclient.animationsmod.property.disableuseanimation.description",
		priority = 975
	)
	public static boolean DisableUseAnimation = false;

//	@ModConfig(
//		type = ConfigType.SWITCH,
//		name = "Flat Item",
//		description = "Renders held items as flat 2D maps.",
//		category = "Item",
//		priority = 800
//	)
//	public static boolean FlatItem = false;

	@ModConfigExtra(
		type = ConfigType.SLIDER,
		name = "lucent.config.lucentclient.animationsmod.property.itemscale.name",
		description = "lucent.config.lucentclient.animationsmod.property.itemscale.description",
		category = "Item",
		min = 0.1, max = 2.0, step = 0.05,
		align = Align.RIGHT,
		priority = 790
	)
	public static double ItemScale = 1.0;

	@ModConfigExtra(
		type = ConfigType.SLIDER,
		name = "lucent.config.lucentclient.animationsmod.property.helditemx.name",
		description = "lucent.config.lucentclient.animationsmod.property.helditemx.description",
		category = "Item",
		min = -2.0, max = 2.0, step = 0.05,
		align = Align.RIGHT,
		priority = 780
	)
	public static double HeldItemX = 0.0;

	@ModConfigExtra(
		type = ConfigType.SLIDER,
		name = "lucent.config.lucentclient.animationsmod.property.helditemy.name",
		description = "lucent.config.lucentclient.animationsmod.property.helditemy.description",
		category = "Item",
		min = -2.0, max = 2.0, step = 0.05,
		align = Align.RIGHT,
		priority = 770
	)
	public static double HeldItemY = 0.0;

	@ModConfigExtra(
		type = ConfigType.SLIDER,
		name = "lucent.config.lucentclient.animationsmod.property.helditemz.name",
		description = "lucent.config.lucentclient.animationsmod.property.helditemz.description",
		category = "Item",
		min = -2.0, max = 2.0, step = 0.05,
		align = Align.RIGHT,
		priority = 760
	)
	public static double HeldItemZ = 0.0;

	@ModConfigExtra(
		type = ConfigType.SLIDER,
		name = "lucent.config.lucentclient.animationsmod.property.helditemyaw.name",
		description = "lucent.config.lucentclient.animationsmod.property.helditemyaw.description",
		category = "Item",
		min = -180.0, max = 180.0, step = 1.0,
		align = Align.RIGHT,
		priority = 750
	)
	public static double HeldItemYaw = 0.0;

	@ModConfigExtra(
		type = ConfigType.SLIDER,
		name = "lucent.config.lucentclient.animationsmod.property.helditempitch.name",
		description = "lucent.config.lucentclient.animationsmod.property.helditempitch.description",
		category = "Item",
		min = -180.0, max = 180.0, step = 1.0,
		align = Align.RIGHT,
		priority = 740
	)
	public static double HeldItemPitch = 0.0;

	@ModConfigExtra(
		type = ConfigType.SLIDER,
		name = "lucent.config.lucentclient.animationsmod.property.helditemroll.name",
		description = "lucent.config.lucentclient.animationsmod.property.helditemroll.description",
		category = "Item",
		min = -180.0, max = 180.0, step = 1.0,
		align = Align.RIGHT,
		priority = 730
	)
	public static double HeldItemRoll = 0.0;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.animationsmod.property.fireoverlay.name",
		description = "lucent.config.lucentclient.animationsmod.property.fireoverlay.description",
		category = "Overlay",
		priority = 700
	)
	public static boolean FireOverlay = true;

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "lucent.config.lucentclient.animationsmod.property.fireheight.name",
		description = "lucent.config.lucentclient.animationsmod.property.fireheight.description",
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
		name = "lucent.config.lucentclient.animationsmod.property.shieldheight.name",
		description = "lucent.config.lucentclient.animationsmod.property.shieldheight.description",
		category = "Shield",
		min = -0.5,
		max = 0.5,
		step = 0.1
	)
	public static float ShieldHeight = 0.0f;

}