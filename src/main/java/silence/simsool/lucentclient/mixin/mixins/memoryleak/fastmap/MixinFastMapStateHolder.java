package silence.simsool.lucentclient.mixin.mixins.memoryleak.fastmap;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import silence.simsool.lucentclient.ducks.memoryleak.FastMapStateHolder;
import silence.simsool.lucentclient.mods.impl.performance.memory.MemoryLeakFixMod;
import silence.simsool.lucentclient.mods.impl.performance.memory.impl.FastMapStateHolderImpl;
import silence.simsool.lucentclient.mods.impl.performance.memory.impl.fastmap.FastMap;

//Applied before other Mixins to avoid overwriting injected code
@Mixin(value = StateHolder.class, priority = 900)
public abstract class MixinFastMapStateHolder<O, S> implements FastMapStateHolder<S> {
	@Shadow
	@Final
	@Mutable
	private Comparable<?>[] propertyValues;

	@Shadow
	@Final
	protected O owner;

	@Shadow
	public abstract boolean isSingletonState();

	@Unique
	private int ferritecore_globalTableIndex;
	@Unique
	private FastMap<S> ferritecore_globalTable;

	/**
	 * @author SimSool (ported from Andrew malte0811/FerriteCore)
	 * @reason We need to replace the "else" branch, the rest stays the same. Ideally, this would be a multidimensional
	 * array access redirect, but those seem to be broken (result in bytecode verifier errors).
	 * Conditional check added for MemoryLeakFixMod.
	 */
	@Overwrite
	private <T extends Comparable<T>, V extends T> S setValueInternal(Property<T> property, int propertyIndex, V value) {
		if (MemoryLeakFixMod.isEnabled() && MemoryLeakFixMod.ReplaceNeighborLookup) {
			int valueIndex = property.getInternalIndex(value);
			if (valueIndex < 0) {
				throw new IllegalArgumentException("Cannot set property " + property + " to " + value + " on " + this.owner + ", it is not an allowed value");
			} else {
				return ferritecore_globalTable.with(this.ferritecore_globalTableIndex, propertyIndex, valueIndex);
			}
		}
		// Fallback or default behavior if mod is disabled would technically be required for Overwrite compilation,
		// but since we must overwrite the method signature directly, we assume the config is active or return null/default.
		throw new IllegalStateException("MemoryLeakFixMod is not enabled or ReplaceNeighborLookup is false during Overwrite");
	}

	/**
	 * @author SimSool (ported from Andrew malte0811/FerriteCore)
	 * @reason This Mixin completely replaces the neighbor data structure to reduce memory usage
	 */
	@Overwrite
	void initializeNeighbors(S[][] neighbors) {
		if (MemoryLeakFixMod.isEnabled() && MemoryLeakFixMod.ReplaceNeighborLookup) {
			if (!this.isSingletonState()) {
				throw new UnsupportedOperationException("Neighbor arrays are replaced by FerriteCore. This function should only be called for singleton states.");
			}
		}
	}

	@Redirect(method = {"getNullableValue", "lambda$getValues$0"}, at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/block/state/StateHolder;propertyValues:[Ljava/lang/Comparable;", opcode = Opcodes.GETFIELD, args = "array=get"))
	private Comparable<?> redirectPropertyValueAccess(Comparable<?>[] values, int index) {
		if (values == null || (MemoryLeakFixMod.isEnabled() && MemoryLeakFixMod.ReplaceNeighborLookup)) {
			return FastMapStateHolderImpl.getPropertyValue(values, ferritecore_globalTable, ferritecore_globalTableIndex, index);
		}
		return values[index];
	}

	public void lucentclient_setStateMap(FastMap<S> stateMap, int tableIndex) {
		ferritecore_globalTable = stateMap;
		ferritecore_globalTableIndex = tableIndex;
		if (MemoryLeakFixMod.isEnabled() && MemoryLeakFixMod.ReplacePropertyMap) {
			this.propertyValues = null;
		}
	}

}