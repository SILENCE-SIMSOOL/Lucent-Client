package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import silence.simsool.lucentclient.mods.impl.graphics.AnimationsMod;

@Mixin(EquipmentLayerRenderer.class)
public class MixinEquipmentLayerRenderer {

	@ModifyArg(
		method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;II)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/OrderedSubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IIILnet/minecraft/client/renderer/texture/UvMapping;I)V"
		),
		index = 5
	)
	private int modifyArmorOverlay(int originalOverlay, @Local(argsOnly = true) Object state) {
		if (AnimationsMod.isEnabled() && AnimationsMod.CustomHitColor && AnimationsMod.ArmorHitColor) {
			if (state instanceof LivingEntityRenderState living && living.hasRedOverlay) {
				return OverlayTexture.pack(OverlayTexture.u(0.0f), OverlayTexture.v(true));
			}
		}
		return originalOverlay;
	}
}