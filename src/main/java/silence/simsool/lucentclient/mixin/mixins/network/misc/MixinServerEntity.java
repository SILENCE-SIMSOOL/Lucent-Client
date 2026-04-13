package silence.simsool.lucentclient.mixin.mixins.network.misc;

import com.google.common.collect.ImmutableList;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(ServerEntity.class)
public class MixinServerEntity {

	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/Collections;emptyList()Ljava/util/List;"))
	public List<Entity> construct$initialPassengersListIsGuavaImmutableList() {
		return ImmutableList.of();
	}

}