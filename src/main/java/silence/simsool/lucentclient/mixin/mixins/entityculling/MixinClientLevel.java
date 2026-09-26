package silence.simsool.lucentclient.mixin.mixins.entityculling;

import static silence.simsool.lucent.Lucent.mc;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.warden.AngerLevel;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import silence.simsool.lucentclient.mixin.accessors.DisplayAccessor;
import silence.simsool.lucentclient.mods.impl.performance.EntityCullingMod;
import silence.simsool.lucentclient.mods.impl.performance.culling.Cullable;
import silence.simsool.lucentclient.mods.impl.performance.culling.CullingHelper;

@Mixin(ClientLevel.class)
public abstract class MixinClientLevel {

	@Inject(method = "tickNonPassenger", at = @At("HEAD"), cancellable = true)
	public void onTickNonPassenger(Entity entity, CallbackInfo ci) {
		if (!EntityCullingMod.isEnabled() || !EntityCullingMod.TickCulling) {
			EntityCullingMod.tickedEntities++;
			return;
		}

		if (EntityCullingMod.ForceDisplayCulling && entity instanceof Display display) {
			processDisplay(display);
		}

		if (CullingHelper.ignoresCulling(entity)
				|| entity == mc.player
				|| entity == mc.getCameraEntity()
				|| entity.isPassenger()
				|| entity.isVehicle()
				|| (entity instanceof AbstractMinecart)) {
			EntityCullingMod.tickedEntities++;
			return;
		}

		if (EntityCullingMod.TICK_CULLING_WHITELIST.contains(entity.getType())
				|| EntityCullingMod.ENTITY_WHITELIST.contains(entity.getType())) {
			EntityCullingMod.tickedEntities++;
			return;
		}

		if (entity instanceof Cullable cull) {
			if (cull.isCulled() || cull.isOutOfCamera()) {
				basicTick(entity);
				EntityCullingMod.skippedEntityTicks++;
				ci.cancel();
				return;
			} else {
				cull.setOutOfCamera(true);
			}
		}

		EntityCullingMod.tickedEntities++;
	}

	@Unique
	private void processDisplay(Display display) {
		if (display.getBoundingBox().getSize() == 0 && display instanceof DisplayAccessor accessor) {
			accessor.invokeSetWidth(3);
			accessor.invokeSetHeight(3);
			display.setPos(display.getX(), display.getY(), display.getZ());
		}
	}

	@Unique
	private void basicTick(Entity entity) {
		entity.setOldPosAndRot();
		++entity.tickCount;
		if (entity instanceof LivingEntity living) {
			living.aiStep();
			if (living.hurtTime > 0) {
				living.hurtTime--;
			}
		}

		var interpolation = entity.getInterpolation();
		if (interpolation != null) {
			interpolation.interpolate();
		}

		if (entity instanceof Warden warden) {
			if (mc.level != null && mc.level.isClientSide() && !warden.isSilent()
					&& warden.tickCount % getWardenHeartBeatDelay(warden) == 0) {
				mc.level.playLocalSound(warden.getX(), warden.getY(), warden.getZ(),
						SoundEvents.WARDEN_HEARTBEAT, warden.getSoundSource(), 5.0F, warden.getVoicePitch(), false);
			}
		}
	}

	@Unique
	private int getWardenHeartBeatDelay(Warden warden) {
		float f = warden.getClientAngerLevel() / AngerLevel.ANGRY.getMinimumAnger();
		return 40 - Mth.floor(Mth.clamp(f, 0.0F, 1.0F) * 30.0F);
	}
}