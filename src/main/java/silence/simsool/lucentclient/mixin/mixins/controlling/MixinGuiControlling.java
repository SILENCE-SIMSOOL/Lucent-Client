package silence.simsool.lucentclient.mixin.mixins.controlling;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import silence.simsool.lucentclient.features.controlling.client.NewKeyBindsScreen;

@Mixin(Gui.class)
public class MixinGuiControlling {
	@Shadow
	private Screen screen;

	@ModifyVariable(method = "setScreen", at = @At("HEAD"), argsOnly = true, name = "screen")
	private Screen lucent$upgradeControlScreen(@Nullable Screen targetScreen) {
		if (targetScreen != null && KeyBindsScreen.class.equals(targetScreen.getClass())) {
			return new NewKeyBindsScreen(this.screen, Minecraft.getInstance().options);
		}
		return targetScreen;
	}
}