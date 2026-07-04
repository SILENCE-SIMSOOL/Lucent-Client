package silence.simsool.lucentclient.mixin.mixins.memoryleak.threaddetect;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.util.ThreadingDetector;
import net.minecraft.world.level.chunk.PalettedContainer;
import silence.simsool.lucentclient.ducks.memoryleak.SmallThreadDetectable;
import silence.simsool.lucentclient.mods.impl.performance.memory.MemoryLeakFixMod;
import silence.simsool.lucentclient.mods.impl.performance.memory.impl.utils.SmallThreadingDetector;

@Mixin(PalettedContainer.class)
public class MixinPalettedContainer implements SmallThreadDetectable {

	@Shadow
	@Final
	@Mutable
	private ThreadingDetector threadingDetector;

	@Unique
	private byte lucentclient$threadingState = UNLOCKED;

	@Inject(method = { "<init>(Ljava/lang/Object;Lnet/minecraft/world/level/chunk/Strategy;)V", "<init>(Lnet/minecraft/world/level/chunk/PalettedContainer;)V", "<init>(Lnet/minecraft/world/level/chunk/Strategy;Lnet/minecraft/world/level/chunk/Configuration;Lnet/minecraft/util/BitStorage;Lnet/minecraft/world/level/chunk/Palette;)V", }, at = @At("TAIL"))
	public void redirectBuildThreadingDetector(CallbackInfo ci) {
		if (MemoryLeakFixMod.isEnabled() && MemoryLeakFixMod.UseSmallThreadingDetector) {
			this.threadingDetector = null;
		}
	}

	@Inject(method = "acquire", at = @At("HEAD"), cancellable = true)
	public void onAcquire(CallbackInfo ci) {
		if (MemoryLeakFixMod.isEnabled() && MemoryLeakFixMod.UseSmallThreadingDetector) {
			SmallThreadingDetector.acquire(this, "PalettedContainer");
			ci.cancel();
		}
	}

	@Inject(method = "release", at = @At("HEAD"), cancellable = true)
	public void onRelease(CallbackInfo ci) {
		if (MemoryLeakFixMod.isEnabled() && MemoryLeakFixMod.UseSmallThreadingDetector) {
			SmallThreadingDetector.release(this);
			ci.cancel();
		}
	}

	@Override
	public byte lucentclient$getState() {
		return lucentclient$threadingState;
	}

	@Override
	public void lucentclient$setState(byte newState) {
		lucentclient$threadingState = newState;
	}

}