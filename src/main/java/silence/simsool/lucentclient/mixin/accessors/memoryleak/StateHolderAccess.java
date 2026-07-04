package silence.simsool.lucentclient.mixin.accessors.memoryleak;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(StateHolder.class)
public interface StateHolderAccess {
	@Accessor
	Property<?>[] getPropertyKeys();

	@Invoker("initializeNeighbors")
	void callInitializeNeighbors(Object[][] neighbors);
}