package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.properties.Property;
import com.mojang.authlib.yggdrasil.YggdrasilServicesKeyInfo;

import silence.simsool.lucentclient.mods.impl.utility.LegacySkinFix;

@Mixin(value = YggdrasilServicesKeyInfo.class, remap = false)
public class MixinYggdrasilSignatureIgnore {

	@Inject(method = "validateProperty", at = @At("HEAD"), cancellable = true, remap = false)
	public void validate(Property property, CallbackInfoReturnable<Boolean> cir) {
		if (LegacySkinFix.isEnabled()) cir.setReturnValue(true);
	}

}