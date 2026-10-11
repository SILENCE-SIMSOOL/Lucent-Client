package silence.simsool.lucentclient.mixin.mixins.asyncparticles.core.weather;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import silence.simsool.lucentclient.features.asyncparticles.config.ConfigHelper;
import silence.simsool.lucentclient.features.asyncparticles.core.Diagnostic;
import silence.simsool.lucentclient.features.asyncparticles.core.Phase;
import silence.simsool.lucentclient.features.asyncparticles.core.particle.tick.AsyncTickBehavior;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.server.level.ParticleStatus;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WeatherEffectRenderer.class)
public class MixinWeatherEffectRenderer {
	@WrapMethod(method = "tickRainParticles")
	private void wrapTickRain(ClientLevel level,
	                          Camera camera,
	                          int ticks,
	                          ParticleStatus particleStatus,
	                          int weatherRadius,
	                          Operation<Void> original) {
		if (ConfigHelper.isTickWeatherAsync()
			&& AsyncTickBehavior.getInstance().isTailTick()) {
			AsyncTickBehavior.getInstance().addTaskEnsureLevelRunning(
				() -> original.call(level, camera, ticks, particleStatus, weatherRadius), Diagnostic::errorAsyncWeatherTick, Phase.WEATHER_TICK);
		} else {
			original.call(level, camera, ticks, particleStatus, weatherRadius);
		}
	}
}