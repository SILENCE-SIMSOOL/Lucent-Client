package silence.simsool.lucentclient.mixin.mixins.network;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.util.ResourceLeakDetector;
import net.minecraft.SharedConstants;
import silence.simsool.lucentclient.mods.impl.performance.network.NetworkFixMod;

@Mixin(SharedConstants.class)
public class MixinSharedConstants {

	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void lucentclient$resourceLeakDetectorDisableConditional(CallbackInfo ci) {
		if (NetworkFixMod.isEnabled() && NetworkFixMod.LeakDetectionOptimize) {
			if (System.getProperty("io.netty.leakDetection.level") == null) {
				ResourceLeakDetector.setLevel(SharedConstants.NETTY_LEAK_DETECTION);
			}
		}
		else ResourceLeakDetector.setLevel(SharedConstants.NETTY_LEAK_DETECTION);
	}

}