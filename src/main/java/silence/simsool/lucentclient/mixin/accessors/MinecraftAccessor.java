package silence.simsool.lucentclient.mixin.accessors;

import java.util.concurrent.CompletableFuture;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.yggdrasil.ProfileResult;

import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import net.minecraft.client.multiplayer.ProfileKeyPairManager;

@Mixin(Minecraft.class)
public interface MinecraftAccessor {

	@Mutable
	@Accessor("user")
	void setUser(User user);

	@Accessor("user")
	User getUser();

	@Mutable
	@Accessor("profileFuture")
	void setProfileFuture(CompletableFuture<ProfileResult> profileFuture);

	@Accessor("userApiService")
	UserApiService getUserApiService();

	@Mutable
	@Accessor("profileKeyPairManager")
	void setProfileKeyPairManager(ProfileKeyPairManager profileKeyPairManager);

}