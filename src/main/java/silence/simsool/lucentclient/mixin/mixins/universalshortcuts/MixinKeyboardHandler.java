package silence.simsool.lucentclient.mixin.mixins.universalshortcuts;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;
import silence.simsool.lucentclient.features.universalshortcuts.UniversalShortcutsUtil;

@Mixin(KeyboardHandler.class)
public abstract class MixinKeyboardHandler {

	@ModifyVariable(method = "handleChunkDebugKeys", at = @At("HEAD"), argsOnly = true)
	private KeyEvent remapChunkDebugKey(KeyEvent keyEvent) {
		return UniversalShortcutsUtil.remapKeyEventToQWERTY(keyEvent);
	}
}