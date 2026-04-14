package silence.simsool.lucentclient.mixin.mixins.network.misc;

import java.util.Collections;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.google.common.collect.ImmutableList;

import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import silence.simsool.lucentclient.mods.impl.performance.network.NetworkFixMod;

@Mixin(ServerEntity.class)
public class MixinServerEntity {

	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/Collections;emptyList()Ljava/util/List;"))
	public List<Entity> initialPassengersListIsGuavaImmutableList() {
		if (NetworkFixMod.isEnabled() && NetworkFixMod.ImmutablePassengers) return ImmutableList.of();
		return Collections.emptyList();
	}

}