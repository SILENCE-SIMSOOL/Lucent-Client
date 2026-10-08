package silence.simsool.lucentclient.mixin.mixins.packmanager;

import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.resources.FallbackResourceManager;
import silence.simsool.lucentclient.mods.impl.utility.packmanager.PackManagerMod;
import silence.simsool.lucentclient.mods.impl.utility.packmanager.utils.FilteredPackResources;

@Mixin(FallbackResourceManager.class)
public class MixinFallbackResourceManager {

	@Shadow
	@Final
	private String namespace;

	@ModifyArgs(method = { "pushFilterOnly", "push(Lnet/minecraft/server/packs/PackResources;)V", "push(Lnet/minecraft/server/packs/PackResources;Ljava/util/function/Predicate;)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/FallbackResourceManager;pushInternal(Ljava/lang/String;Lnet/minecraft/server/packs/PackResources;Ljava/util/function/Predicate;)V"))
	private void filterHypixelPack(Args args) {
		if (PackManagerMod.isEnabled() && PackManagerMod.DisablePackOverride) {
			if (!Identifier.DEFAULT_NAMESPACE.equals(namespace)) return;
			String packId = args.get(0);
			if (!PackManagerMod.fromHypixelPack(packId)) return;

			PackResources pack = args.get(1);
			if (pack != null) args.set(1, new FilteredPackResources(pack));
			else args.set(2, (Predicate<Identifier>) id -> false);
		}
	}

}