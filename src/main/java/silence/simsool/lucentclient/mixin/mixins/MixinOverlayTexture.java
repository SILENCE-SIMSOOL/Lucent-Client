package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import silence.simsool.lucentclient.mods.impl.graphics.damagetint.DamageTintHelper;

@Mixin(OverlayTexture.class)
public class MixinOverlayTexture {

	@Shadow
	@Final
	private DynamicTexture texture;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void onInit(CallbackInfo ci) {
		DamageTintHelper.init(this.texture);
	}
}