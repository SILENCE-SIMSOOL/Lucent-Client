package silence.simsool.lucentclient.mixin.accessors.memoryleak;

import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DiscreteVoxelShape.class)
public interface DiscreteVSAccess {
	@Accessor
	int getXSize();

	@Accessor
	int getYSize();

	@Accessor
	int getZSize();
}