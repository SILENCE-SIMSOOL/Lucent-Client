package silence.simsool.lucentclient.mixin.mixins.entityculling;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import silence.simsool.lucentclient.mods.impl.performance.EntityCullingMod;
import silence.simsool.lucentclient.mods.impl.performance.culling.Cullable;
import silence.simsool.lucentclient.mods.impl.performance.culling.CullingHelper;

@Mixin(BlockEntityRenderDispatcher.class)
public abstract class MixinBlockEntityRenderDispatcher {

	@Shadow
	public abstract <E extends BlockEntity> BlockEntityRenderer<?, ?> getRenderer(E blockEntity);

	@Inject(method = "tryExtractRenderState", at = @At("HEAD"), cancellable = true)
	public void onTryExtractRenderState(
			BlockEntity blockEntity,
			float partialTicks,
			ModelFeatureRenderer.CrumblingOverlay crumblingOverlay,
			CallbackInfoReturnable<BlockEntityRenderState> cir
	) {
		if (!EntityCullingMod.isEnabled() || !EntityCullingMod.CullBlockEntities) {
			EntityCullingMod.renderedBlockEntities++;
			return;
		}

		BlockEntityRenderer<?, ?> renderer = getRenderer(blockEntity);
		if (renderer == null) {
			return;
		}

		if (renderer.shouldRenderOffScreen()) {
			EntityCullingMod.renderedBlockEntities++;
			return;
		}

		if (EntityCullingMod.isBlockEntityWhitelisted(blockEntity)) {
			EntityCullingMod.renderedBlockEntities++;
			return;
		}

		Frustum frustum = EntityCullingMod.frustum;
		if (EntityCullingMod.BlockEntityFrustumCulling && frustum != null
				&& !frustum.isVisible(CullingHelper.setupAABB(blockEntity, blockEntity.getBlockPos()))) {
			EntityCullingMod.skippedBlockEntities++;
			cir.setReturnValue(null);
			return;
		}

		if (blockEntity instanceof Cullable cullable) {
			if (!cullable.isForcedVisible() && cullable.isCulled()) {
				EntityCullingMod.skippedBlockEntities++;
				cir.setReturnValue(null);
				return;
			}
			EntityCullingMod.renderedBlockEntities++;
			cullable.setOutOfCamera(false);
		}
	}
}