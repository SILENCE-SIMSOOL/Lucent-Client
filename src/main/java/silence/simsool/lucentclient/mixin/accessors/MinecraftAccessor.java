package silence.simsool.lucentclient.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;

@Mixin(Minecraft.class)
public interface MinecraftAccessor {

	@Accessor("user")
	void setUser(User user);

	@Accessor("user")
	User getUser();

}