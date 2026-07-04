package silence.simsool.lucentclient.mods;

import java.util.Arrays;

import silence.simsool.lucent.config.ModManager;
import silence.simsool.lucentclient.mods.impl.graphics.AnimationsMod;
import silence.simsool.lucentclient.mods.impl.graphics.BlockOverlayMod;
import silence.simsool.lucentclient.mods.impl.graphics.DeathAnimationMod;
import silence.simsool.lucentclient.mods.impl.graphics.FullbrightMod;
import silence.simsool.lucentclient.mods.impl.graphics.HideFallingBlockMod;
import silence.simsool.lucentclient.mods.impl.graphics.HurtCamMod;
import silence.simsool.lucentclient.mods.impl.graphics.ParticlesMod;
import silence.simsool.lucentclient.mods.impl.graphics.TimeChangerMod;
import silence.simsool.lucentclient.mods.impl.hud.ArmorStatusMod;
import silence.simsool.lucentclient.mods.impl.hud.CPSMod;
import silence.simsool.lucentclient.mods.impl.hud.CoordinatesMod;
import silence.simsool.lucentclient.mods.impl.hud.FPSMod;
import silence.simsool.lucentclient.mods.impl.hud.KeystrokesMod;
import silence.simsool.lucentclient.mods.impl.hud.PingMod;
import silence.simsool.lucentclient.mods.impl.hud.PotionEffectsMod;
import silence.simsool.lucentclient.mods.impl.hud.TPSMod;
import silence.simsool.lucentclient.mods.impl.hud.VanillaHUDMod;
import silence.simsool.lucentclient.mods.impl.performance.EntityCullingMod;
import silence.simsool.lucentclient.mods.impl.performance.HUDCullingMod;
import silence.simsool.lucentclient.mods.impl.performance.memory.MemoryLeakFixMod;
import silence.simsool.lucentclient.mods.impl.performance.network.NetworkFixMod;
import silence.simsool.lucentclient.mods.impl.utility.AlwaysSprintMod;
import silence.simsool.lucentclient.mods.impl.utility.BetterF5Mod;
import silence.simsool.lucentclient.mods.impl.utility.ChattingMod;
import silence.simsool.lucentclient.mods.impl.utility.TntTimerMod;
import silence.simsool.lucentclient.mods.impl.utility.ZoomMod;
import silence.simsool.lucentclient.mods.impl.utility.scrollabletooltips.ScrollableTooltipsMod;

public class LucentClientModRegister {

	public static void register(ModManager config) {

		Arrays.asList(

			// Graphics
			new AnimationsMod(),
			new BlockOverlayMod(),
			new DeathAnimationMod(),
			new FullbrightMod(),
			new HideFallingBlockMod(),
			new HurtCamMod(),
			//new NametagsMod(),
			new ParticlesMod(),
			//new PlayerModelMod(),
			new TimeChangerMod(),

			// HUD
			new ArmorStatusMod(),
			new FPSMod(),
			new CPSMod(),
			new TPSMod(),
			new PingMod(),
			new CoordinatesMod(),
			new KeystrokesMod(),
			new PotionEffectsMod(),
			new VanillaHUDMod(),

			// Performance
			new EntityCullingMod(),
			new HUDCullingMod(),
			new MemoryLeakFixMod(),
			new NetworkFixMod(),

			// Utility
			new BetterF5Mod(),
			new ScrollableTooltipsMod(),
			new TntTimerMod(),
			new AlwaysSprintMod(),
			new ZoomMod(),
			new ChattingMod()

		).forEach(config::register);

	}

}