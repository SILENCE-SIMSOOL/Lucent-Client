package silence.simsool.lucentclient.mixin.mixins.controlling;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyBindsScreen.class)
public interface AccessKeyBindsScreen {

	@Accessor("keyBindsList")
	KeyBindsList lucent$getKeyBindsList();

	@Accessor("keyBindsList")
	void lucent$setKeyBindsList(KeyBindsList list);

	@Accessor("resetButton")
	Button lucent$getResetButton();

	@Accessor("resetButton")
	void lucent$setResetButton(Button button);

}