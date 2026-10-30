package silence.simsool.lucentclient.mixin.mixins.universalshortcuts;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.input.KeyEvent;
import silence.simsool.lucentclient.features.universalshortcuts.UniversalShortcutsUtil;

@Mixin(KeyEvent.class)
public abstract class MixinKeyEvent implements InputWithModifiers {

	@Override
	public boolean isSelectAll() {
		return UniversalShortcutsUtil.isKey(this.input(), "A")
			&& this.hasControlDown()
			&& !this.hasShiftDown()
			&& !this.hasAltDown();
	}

	@Override
	public boolean isCopy() {
		return UniversalShortcutsUtil.isKey(this.input(), "C")
			&& this.hasControlDown()
			&& !this.hasShiftDown()
			&& !this.hasAltDown();
	}

	@Override
	public boolean isPaste() {
		return UniversalShortcutsUtil.isKey(this.input(), "V")
			&& this.hasControlDown()
			&& !this.hasShiftDown()
			&& !this.hasAltDown();
	}

	@Override
	public boolean isCut() {
		return UniversalShortcutsUtil.isKey(this.input(), "X")
			&& this.hasControlDown()
			&& !this.hasShiftDown()
			&& !this.hasAltDown();
	}

}