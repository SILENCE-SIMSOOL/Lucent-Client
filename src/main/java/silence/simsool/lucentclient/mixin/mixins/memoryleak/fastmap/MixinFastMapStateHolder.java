package silence.simsool.lucentclient.mixin.mixins.memoryleak.fastmap;

import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import silence.simsool.lucentclient.ducks.memoryleak.FastMapStateHolder;
import silence.simsool.lucentclient.mods.impl.performance.memory.MemoryLeakFixMod;
import silence.simsool.lucentclient.mods.impl.performance.memory.impl.FastMapStateHolderImpl;
import silence.simsool.lucentclient.mods.impl.performance.memory.impl.fastmap.FastMap;

@Mixin(StateHolder.class)
public abstract class MixinFastMapStateHolder<O, S> implements FastMapStateHolder<S> {

	@Mutable
	@Shadow
	@Final
	private Reference2ObjectArrayMap<Property<?>, Comparable<?>> values;

	@Shadow
	private Map<Property<?>, S[]> neighbours;

	@Shadow
	@Final
	protected O owner;
	private int ferritecore_globalTableIndex;
	private FastMap<S> lucentclient_globalTable;

	@SuppressWarnings("unchecked")
	@Inject(method = "setValueInternal", at = @At("HEAD"), cancellable = true)
	private <T extends Comparable<T>, V extends T> void onSetValueInternal(Property<T> property, V newValue, Comparable<?> oldValue, CallbackInfoReturnable<S> cir) {
		if (MemoryLeakFixMod.isEnabled() && MemoryLeakFixMod.ReplaceNeighborLookup) {
			if (oldValue.equals(newValue)) cir.setReturnValue((S) this);
			else {
				S newState = lucentclient_globalTable.with(ferritecore_globalTableIndex, property, newValue);
				if (newState == null) throw new IllegalArgumentException("Cannot set property " + property + " to " + newValue + " on " + this.owner + ", it is not an allowed value");
				else cir.setReturnValue(newState);
			}
		}
	}

	@Inject(method = "populateNeighbours", at = @At("HEAD"), cancellable = true)
	public void onPopulateNeighbours(Map<Map<Property<?>, Comparable<?>>, S> states, CallbackInfo ci) {
		if (MemoryLeakFixMod.isEnabled() && MemoryLeakFixMod.ReplaceNeighborLookup) {
			FastMapStateHolderImpl.populateNeighbors(states, this);
			ci.cancel();
		}
	}

	@Override
	public FastMap<S> getStateMap() {
		return lucentclient_globalTable;
	}

	@Override
	public int getStateIndex() {
		return ferritecore_globalTableIndex;
	}

	@Override
	public Reference2ObjectMap<Property<?>, Comparable<?>> getVanillaPropertyMap() {
		return values;
	}

	@Override
	public void replacePropertyMap(Reference2ObjectMap<Property<?>, Comparable<?>> newMap) {
		values = (Reference2ObjectArrayMap<Property<?>, Comparable<?>>) newMap;
	}

	@Override
	public void setStateMap(FastMap<S> newValue) {
		lucentclient_globalTable = newValue;
	}

	@Override
	public void setStateIndex(int newValue) {
		ferritecore_globalTableIndex = newValue;
	}

	@Override
	public void setNeighborMap(Map<Property<?>, S[]> table) {
		neighbours = table;
	}

	@Override
	public Map<Property<?>, S[]> getNeighborMap() {
		return neighbours;
	}

}