package silence.simsool.lucentclient.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.util.FormattedCharSequence;

@Mixin(ClientTextTooltip.class)
public interface AccessorOrderedTextTooltipComponent {

	@Accessor
	FormattedCharSequence getText();

}