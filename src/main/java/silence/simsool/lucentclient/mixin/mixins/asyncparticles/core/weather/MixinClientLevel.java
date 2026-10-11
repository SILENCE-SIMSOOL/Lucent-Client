package silence.simsool.lucentclient.mixin.mixins.asyncparticles.core.weather;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import silence.simsool.lucentclient.features.asyncparticles.config.ConfigHelper;
import silence.simsool.lucentclient.features.asyncparticles.core.Diagnostic;
import silence.simsool.lucentclient.features.asyncparticles.core.Phase;
import silence.simsool.lucentclient.features.asyncparticles.core.particle.tick.AsyncTickBehavior;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientLevel.class)
public class MixinClientLevel {
	@WrapMethod(method = "tickWeatherEffects")
	private void wrapTickRain(Operation<Void> original) {
		if (ConfigHelper.isTickWeatherAsync()) {
			AsyncTickBehavior.getInstance().addTaskEnsureLevelRunning(original::call, Diagnostic::errorAsyncWeatherTick, Phase.WEATHER_TICK);
		} else {
			original.call();
		}
	}
}