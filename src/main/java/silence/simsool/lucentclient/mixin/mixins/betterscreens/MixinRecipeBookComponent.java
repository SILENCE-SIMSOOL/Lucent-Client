package silence.simsool.lucentclient.mixin.mixins.betterscreens;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import silence.simsool.lucentclient.mods.impl.utility.BetterScreensMod;

@Mixin(RecipeBookComponent.class)
public class MixinRecipeBookComponent {

	@ModifyReturnValue(method = "updateScreenPosition", at = @At("RETURN"))
	private int onUpdateScreenPosition(int original, int width, int imageWidth) {
		if (BetterScreensMod.isEnabled() && BetterScreensMod.RemoveRecipeBookShift) {
			return (width - imageWidth) / 2;
		}
		return original;
	}

}