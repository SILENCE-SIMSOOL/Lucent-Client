package silence.simsool.lucentclient.mixin.mixins.memoryleak;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.HitResult;
import silence.simsool.lucentclient.mods.impl.performance.MemoryLeakFixMod;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public abstract class MixinMinecraft_TargetClear {

	@Shadow
	@Nullable
	public Entity crosshairPickEntity;

	@Shadow
	@Nullable
	public HitResult hitResult;

	@Inject(method = "tick", at = @At("HEAD"))
	private void resetTarget(CallbackInfo ci) {
		if (MemoryLeakFixMod.isEnabled() && MemoryLeakFixMod.TargetCleanup) {
			this.crosshairPickEntity = null;
			this.hitResult = null;
		}
	}

}