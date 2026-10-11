package silence.simsool.lucentclient.mixin.mixins.blackbarconcealer;

import net.minecraft.core.ClientAsset;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.PlayerSkin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerSkin.class)
public abstract class MixinPlayerSkin {

	@Shadow
	@Final
	private ClientAsset.Texture body;

	@Inject(method = "model", at = @At("RETURN"), cancellable = true)
	public void onGetModel(CallbackInfoReturnable<PlayerModelType> cir) {
		if (!(body instanceof ClientAsset.DownloadedTexture)) {
			return;
		}

		if (cir.getReturnValue() == PlayerModelType.WIDE && !hasThiccArms()) {
			cir.setReturnValue(PlayerModelType.SLIM);
		}
	}

	@Unique
	private boolean hasThiccArms() {
		return body.texturePath().getPath().startsWith("thicc");
	}
}