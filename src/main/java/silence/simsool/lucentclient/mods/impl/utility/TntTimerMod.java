package silence.simsool.lucentclient.mods.impl.utility;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

@ModConfig.CategoryPriority(name = "General", priority = 1000)
@ModConfig.CategoryPriority(name = "Text Style", priority = 500)
@ModConfig.CategoryPriority(name = "Background", priority = 100)
public class TntTimerMod extends Mod {

	public TntTimerMod() {
		super(
				"TNT Timer", "Shows the remaining time before TNT explodes.",
				"Utility",
				"tnt, time, explode",
				LucentClientUtils.getModIcon("tnt_timer")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(TntTimerMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "Show Background", 
		description = "Displays a background box behind the TNT timer text.",
		category = "Background",
		priority = 2
	)
	public static boolean ShowBackground = true;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "Text Shadow", 
		description = "Adds a shadow effect to the TNT timer text.",
		category = "Text Style",
		priority = 1
	)
	public static boolean TextShadow = true;

	{
//		WorldRenderEvents.AFTER_ENTITIES.register(context -> {
//			if (isEnabled()) {
//				if (mc.level == null || mc.player == null) return;
//
//				PoseStack poseStack = context.matrices();
//				MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
//				Font font = mc.font;
//
//				Camera camera = mc.gameRenderer.getMainCamera();
//				float partialTick = mc.getDeltaTracker().getGameTimeDeltaPartialTick(true);
//
//				for (Entity entity : mc.level.entitiesForRendering()) {
//					if (entity instanceof PrimedTnt tnt) {
//						int ticks = tnt.getFuse();
//						double time = ticks / 20.0;
//						String text = String.format("%.2fs", time);
//
//						// [?듭떖 ?섏젙] cameraPos瑜?鍮쇱? 留먭퀬 ?ㅼ젣 ?붾뱶 醫뚰몴 蹂닿컙媛믩쭔 ?ъ슜
//						// WorldRenderContext??matrices()???대? 移대찓???꾩튂媛 ?곸슜???곹깭?????덉쓬
//						double x = Mth.lerp((double)partialTick, tnt.xo, tnt.getX());
//						double y = Mth.lerp((double)partialTick, tnt.yo, tnt.getY()) + tnt.getBbHeight() + 0.5;
//						double z = Mth.lerp((double)partialTick, tnt.zo, tnt.getZ());
//	
//						poseStack.pushPose();
//						poseStack.translate(x, y, z);
//						
//						poseStack.mulPose(camera.rotation());
//						poseStack.scale(-0.025F, -0.025F, 0.025F);
//
//						int color = time < 1.0 ? 0xFFFF5555 : (time < 2.0 ? 0xFFFFFF55 : 0xFF55FF55);
//						float xOffset = (float)(-font.width(text) / 2);
//
//						// ?ш낵(SEE_THROUGH)瑜??⑥빞 吏?뺤뿉 臾삵???蹂댁엫
//						font.drawInBatch(
//							text, xOffset, 0, color, TextShadow, 
//							poseStack.last().pose(), bufferSource, 
//							Font.DisplayMode.SEE_THROUGH, 0, 15728880
//						);
//
//						poseStack.popPose();
//					}
//				}
//				bufferSource.endBatch();
//			}
//		});
	}

}
