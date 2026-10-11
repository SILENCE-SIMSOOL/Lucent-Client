package silence.simsool.lucentclient.mixin.mixins.controlling;

import java.util.List;
import net.minecraft.client.gui.components.AbstractSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractSelectionList.class)
public interface AccessAbstractSelectionList {
	@Accessor("children")
	<E> List<E> lucent$getChildren();
}