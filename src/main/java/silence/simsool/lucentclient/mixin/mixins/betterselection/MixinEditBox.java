package silence.simsool.lucentclient.mixin.mixins.betterselection;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import silence.simsool.lucentclient.features.betterselection.WordMachine;

@Mixin(EditBox.class)
public abstract class MixinEditBox extends AbstractWidget {

	private MixinEditBox(int x, int y, int width, int height, Component message) {
		super(x, y, width, height, message);
	}

	@Shadow
	@Final
	private Font font;

	@Shadow
	private String value;

	@Shadow
	private long focusedTime;

	@Unique
	private int lucent$traverseWord(int directedCount, int index) {
		int direction = directedCount > 0 ? 1 : -1;
		int readOffset = directedCount > 0 ? 0 : -1;
		int count = Mth.abs(directedCount);

		try {
			for (int i = 0; i < count; i++) {
				if (WordMachine.isWhitespaceCharacter(this.value.charAt(index + readOffset))) {
					index += direction;
				}

				WordMachine machine = WordMachine.startRead(this.value.charAt(index + readOffset));
				while (machine != WordMachine.DONE) {
					index += direction;
					machine = machine.read(this.value.charAt(index + readOffset));
				}
			}
			return index;
		} catch (IndexOutOfBoundsException exception) {
			return index <= 0 ? 0 : this.value.length();
		}
	}

	@Inject(method = "getWordPosition(IIZ)I", at = @At("HEAD"), cancellable = true)
	private void onGetWordPosition(int directedCount, int index, boolean skipWhitespaceAfterWord, CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(lucent$traverseWord(directedCount, index));
	}

	@Inject(method = "setCursorPosition", at = @At("HEAD"))
	private void onSetCursorPosition(int position, CallbackInfo ci) {
		this.focusedTime = Util.getMillis();
	}

	@Unique
	private static int lucent$nearestCharacterBoundary(Font font, String text, int x) {
		String prefix = font.plainSubstrByWidth(text, x);
		int clickedCharacterIndex = prefix.length();

		if (clickedCharacterIndex >= text.length()) {
			return clickedCharacterIndex;
		}

		int prefixWidth = font.width(prefix);
		int clickedCharacterWidth = font.width(String.valueOf(text.charAt(clickedCharacterIndex)));
		int spaceLeft = x - prefixWidth;
		int spaceRight = prefixWidth + clickedCharacterWidth - x;
		return spaceLeft > spaceRight ? clickedCharacterIndex + 1 : clickedCharacterIndex;
	}

	@Redirect(method = "findClickedPositionInText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;plainSubstrByWidth(Ljava/lang/String;I)Ljava/lang/String;"))
	private String onFindAppropriateCursorPosition(Font instance, String displayedText, int x) {
		return displayedText.substring(0, lucent$nearestCharacterBoundary(this.font, displayedText, x));
	}
}