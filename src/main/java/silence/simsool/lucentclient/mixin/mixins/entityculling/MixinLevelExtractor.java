package silence.simsool.lucentclient.mixin.mixins.entityculling;

import static silence.simsool.lucent.Lucent.mc;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.extract.LevelExtractor;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import silence.simsool.lucentclient.mixin.accessors.LivingEntityRendererAccessor;
import silence.simsool.lucentclient.mods.impl.performance.EntityCullingMod;
import silence.simsool.lucentclient.mods.impl.performance.culling.Cullable;
import silence.simsool.lucentclient.mods.impl.performance.culling.CullingHelper;

@Mixin(LevelExtractor.class)
public abstract class MixinLevelExtractor {

	@Inject(at = @At("HEAD"), method = "extractVisibleEntities")
	private void onExtractVisibleEntities(
			Camera camera,
			Frustum frustum,
			DeltaTracker deltaTracker,
			LevelRenderState levelRenderState,
			CallbackInfo ci
	) {
		EntityCullingMod.frustum = frustum;
	}

	@Inject(at = @At("HEAD"), method = "extractEntity", cancellable = true)
	private void onExtractEntity(
			Entity entity,
			float partialTick,
			CallbackInfoReturnable<EntityRenderState> cir
	) {
		if (!EntityCullingMod.isEnabled()) {
			return;
		}

		if (entity instanceof Cullable cullable) {
			if (!cullable.isForcedVisible() && cullable.isCulled() && !CullingHelper.ignoresCulling(entity)) {
				EntityCullingMod.actualCulledEntitiesCurrentFrame++;

				EntityRenderState state = processNametag(entity, partialTick);
				cir.setReturnValue(state);
				return;
			}
			cullable.setOutOfCamera(false);
		}
	}

	private static EntityRenderState processNametag(Entity entity, float partialTick) {
		EntityRenderState state = new EntityRenderState();
		state.entityType = EntityTypes.INTERACTION;
		state.x = Mth.lerp(partialTick, entity.xOld, entity.getX());
		state.y = Mth.lerp(partialTick, entity.yOld, entity.getY());
		state.z = Mth.lerp(partialTick, entity.zOld, entity.getZ());
		state.isInvisible = true;

		if (EntityCullingMod.RenderNametagsThroughWalls && entity.shouldShowName()) {
			if (entity instanceof LivingEntity living) {
				var renderer = mc.getEntityRenderDispatcher().getRenderer(living);
				double d = mc.getCameraEntity() != null ? mc.getCameraEntity().distanceToSqr(entity) : 0;
				double nameTagDistance = living.getAttributeValue(Attributes.NAME_TAG_DISTANCE);

				if (renderer instanceof LivingEntityRendererAccessor accessor
						&& accessor.invokeShouldShowName(living, d) && !entity.isDiscrete()) {
					Component display = entity.belowNameDisplay();
					if (d < 100 && display != null) {
						AvatarRenderState avatarState = new AvatarRenderState();
						avatarState.entityType = EntityTypes.PLAYER;
						avatarState.scoreText = display;
						avatarState.isInvisibleToPlayer = true;
						avatarState.x = state.x;
						avatarState.y = state.y;
						avatarState.z = state.z;
						avatarState.isInvisible = true;
						state = avatarState;
					}

					if (d < nameTagDistance * nameTagDistance) {
						state.nameTag = entity.getDisplayName();
						state.nameTagAttachment = entity.getAttachments().getNullable(
								EntityAttachment.NAME_TAG, 0, entity.getYRot(partialTick));
					}
				}
			} else {
				state.nameTag = entity.getDisplayName();
				state.nameTagAttachment = entity.getAttachments().getNullable(
						EntityAttachment.NAME_TAG, 0, entity.getYRot(partialTick));
			}
		}

		return state;
	}
}