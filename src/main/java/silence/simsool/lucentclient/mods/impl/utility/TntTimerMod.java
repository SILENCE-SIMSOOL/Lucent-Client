package silence.simsool.lucentclient.mods.impl.utility;

import static silence.simsool.lucent.Lucent.mc;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.phys.Vec3;
import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;

@ModConfig.CategoryPriority(name = "General", priority = 1000)
public class TntTimerMod extends Mod {

	public TntTimerMod() {
		super("TNT Timer", "Shows the remaining time before TNT explodes.", "Utility", "tnt, time, explode", "lucid:tnt");

		WorldRenderEvents.AFTER_ENTITIES.register(context -> {
			if (!isEnabled) return;
			if (mc.level == null) return;

			PoseStack poseStack = context.matrices();
			MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
			Font font = mc.font;
			Vec3 cameraPos = context.gameRenderer().getMainCamera().position();

			for (Entity entity : mc.level.entitiesForRendering()) {
				if (entity instanceof PrimedTnt tnt) {
					int ticks = tnt.getFuse();
					double time = ticks / 20.0;
					String text = String.format("%.2fs", time);
					
					double x = tnt.getX() - cameraPos.x();
					double y = tnt.getY() + tnt.getBbHeight() + 0.5 - cameraPos.y();
					double z = tnt.getZ() - cameraPos.z();

					poseStack.pushPose();
					poseStack.translate(x, y, z);
					poseStack.mulPose(mc.gameRenderer.getMainCamera().rotation());
					poseStack.scale(-0.025F, -0.025F, 0.025F);

					int color = time < 1.0 ? 0xFF0000 : (time < 2.0 ? 0xFFFF00 : 0x00FF00);
					float offset = (float)(-font.width(text) / 2);
					font.drawInBatch(text, offset, 0f, color, false, poseStack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, 0xF000F0);
					poseStack.popPose();
				}
			}
			bufferSource.endBatch();
		});
	}

	public static boolean isEnabled() {
		return Lucent.config.getModule(TntTimerMod.class).isEnabled;
	}

}