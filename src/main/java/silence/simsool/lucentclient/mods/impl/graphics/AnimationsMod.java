package silence.simsool.lucentclient.mods.impl.graphics;

import silence.simsool.lucent.general.utils.LucentCategory;

import java.awt.Color;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
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
		min = 1, max = 16, step = 1,
		priority = 990
	)
	public static int SwingSpeed = 8;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.animationsmod.property.inplaceswing.name",
		description = "lucent.config.lucentclient.animationsmod.property.inplaceswing.description",
		priority = 988
	)
	public static boolean InPlaceSwing = false;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.animationsmod.property.nohandsway.name",
		description = "lucent.config.lucentclient.animationsmod.property.nohandsway.description",
		priority = 986
	)
	public static boolean NoHandSway = false;

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
		name = "lucent.config.lucentclient.animationsmod.property.disableentityclickanimation.name",
		description = "lucent.config.lucentclient.animationsmod.property.disableentityclickanimation.description",
		priority = 975
	)
	public static boolean DisableEntityClickAnimation = false;

//	@ModConfig(
//		type = ConfigType.SWITCH,
//		name = "Flat Item",
//		description = "Renders held items as flat 2D maps.",
//		category = "Item",
//		priority = 800
//	)
//	public static boolean FlatItem = false;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.animationsmod.property.changeemptyhand.name",
		description = "lucent.config.lucentclient.animationsmod.property.changeemptyhand.description",
		category = "Item",
		priority = 795
	)
	public static boolean ChangeEmptyHand = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.animationsmod.property.changeholdingmap.name",
		description = "lucent.config.lucentclient.animationsmod.property.changeholdingmap.description",
		category = "Item",
		priority = 794
	)
	public static boolean ChangeHoldingMap = true;

	@ModConfigExtra(
		type = ConfigType.SLIDER,
		name = "lucent.config.lucentclient.animationsmod.property.itemscale.name",
		description = "lucent.config.lucentclient.animationsmod.property.itemscale.description",
		category = "Item",
		min = 0.1, max = 2.0, step = 0.01,
		align = Align.RIGHT,
		priority = 790
	)
	public static double ItemScale = 1.0;

	@ModConfigExtra(
		type = ConfigType.SLIDER,
		name = "lucent.config.lucentclient.animationsmod.property.helditemx.name",
		description = "lucent.config.lucentclient.animationsmod.property.helditemx.description",
		category = "Item",
		min = -1.0, max = 1.0, step = 0.01,
		align = Align.RIGHT,
		priority = 780
	)
	public static double HeldItemX = 0.0;

	@ModConfigExtra(
		type = ConfigType.SLIDER,
		name = "lucent.config.lucentclient.animationsmod.property.helditemy.name",
		description = "lucent.config.lucentclient.animationsmod.property.helditemy.description",
		category = "Item",
		min = -1.0, max = 1.0, step = 0.01,
		align = Align.RIGHT,
		priority = 770
	)
	public static double HeldItemY = 0.0;

	@ModConfigExtra(
		type = ConfigType.SLIDER,
		name = "lucent.config.lucentclient.animationsmod.property.helditemz.name",
		description = "lucent.config.lucentclient.animationsmod.property.helditemz.description",
		category = "Item",
		min = -1.0, max = 1.0, step = 0.01,
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
		min = 0.0, max = 1.0, step = 0.01,
		priority = 690
	)
	public static double FireHeight = 0.5;

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "lucent.config.lucentclient.animationsmod.property.shieldheight.name",
		description = "lucent.config.lucentclient.animationsmod.property.shieldheight.description",
		category = "Shield",
		min = -0.5, max = 0.5, step = 0.1
	)
	public static float ShieldHeight = 0.0f;

	private static boolean swinging = false;
	private static int swingTimeTick = 0;
	private static float attackAnim = 0.0f;
	private static float prevAttackAnim = 0.0f;

	@Override
	public void onWorldLoad() {
		resetSwing();
	}

	public static void resetSwing() {
		swinging = false;
		swingTimeTick = 0;
		attackAnim = 0.0f;
		prevAttackAnim = 0.0f;
	}

	public static int getCurrentSwingDuration() {
		LocalPlayer player = Lucent.mc.player;
		if (IgnoreHaste || player == null) return 6;
		if (MobEffectUtil.hasDigSpeed(player)) {
			return 6 - (1 + MobEffectUtil.getDigSpeedAmplification(player));
		} else {
			MobEffectInstance fatigue = player.getEffect(MobEffects.MINING_FATIGUE);
			int amp = (fatigue != null) ? fatigue.getAmplifier() : -1;
			return 6 + (1 + amp) * 2;
		}
	}

	public static double getSwingTime() {
		double speed = Math.pow(2.0, (SwingSpeed - 8) / 4.0);
		return swingTimeTick * speed;
	}

	public static void onSwing() {
		if (!isEnabled()) return;
		int total = getCurrentSwingDuration();
		if (swinging && swingTimeTick >= 0 && getSwingTime() < total / 2.0) return;
		swingTimeTick = -1;
		swinging = true;
	}

	public static void onUpdateSwingTime() {
		if (!isEnabled()) return;

		prevAttackAnim = attackAnim;
		int total = getCurrentSwingDuration();

		if (swinging) {
			swingTimeTick++;
			if (getSwingTime() >= total) {
				swingTimeTick = 0;
				swinging = false;
			}
		} else {
			swingTimeTick = 0;
		}

		attackAnim = (float) (getSwingTime() / (double) total);
	}

	public static float getSwingAnimation(float partialTick) {
		if (!isEnabled()) return 0.0f;
		float d = attackAnim - prevAttackAnim;
		if (d < 0.0f) d += 1.0f;
		return prevAttackAnim + d * partialTick;
	}

	public static void applyTransformations(PoseStack poseStack) {
		if (!isEnabled()) return;

		float pitch = (float) HeldItemPitch;
		float yaw = (float) HeldItemYaw;
		float roll = (float) HeldItemRoll;

		if (pitch != 0.0f) poseStack.mulPose(Axis.XP.rotationDegrees(pitch));
		if (yaw != 0.0f) poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
		if (roll != 0.0f) poseStack.mulPose(Axis.ZP.rotationDegrees(roll));

		float transX = (float) HeldItemX;
		float transY = (float) HeldItemY;
		float transZ = (float) HeldItemZ;

		if (transX != 0.0f || transY != 0.0f || transZ != 0.0f) {
			poseStack.translate(transX, transY, transZ);
		}
	}

	public static float getItemScale() {
		if (!isEnabled()) return 1.0f;
		float scale = (float) ItemScale;
		if (scale <= 0.01f) return 1.0f;
		return scale;
	}

	public static void applyScale(PoseStack poseStack) {
		if (!isEnabled()) return;
		float scale = getItemScale();
		if (scale != 1.0f) poseStack.scale(scale, scale, scale);
	}

}