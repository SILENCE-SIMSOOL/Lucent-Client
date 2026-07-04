package silence.simsool.lucentclient.mods.impl.performance.memory.impl;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
import net.minecraft.world.level.block.state.properties.Property;
import silence.simsool.lucentclient.ducks.memoryleak.FastMapStateHolder;
import silence.simsool.lucentclient.mods.impl.performance.memory.MemoryLeakFixMod;
import silence.simsool.lucentclient.mods.impl.performance.memory.impl.fastmap.FastMap;
import silence.simsool.lucentclient.mods.impl.performance.memory.impl.fastmap.neighbormap.CrashNeighborMap;
import silence.simsool.lucentclient.mods.impl.performance.memory.impl.fastmap.neighbormap.FastmapNeighborMap;

public class FastMapStateHolderImpl {
	public static final ThreadLocal<Map<Map<Property<?>, Comparable<?>>, ?>> LAST_STATE_MAP = new ThreadLocal<>();
	public static final ThreadLocal<FastMap<?>> LAST_FAST_STATE_MAP = new ThreadLocal<>();

	/**
	 * Set up the {@link FastMap} used by the given {@link FastMapStateHolder} to
	 * handle neighbors and property lookups. This is called in a loop for each
	 * {@link net.minecraft.world.level.block.state.StateDefinition}, so all state
	 * holders of a given container will use the same {@link FastMap} instance.
	 */
	@SuppressWarnings("unchecked")
	public static <S> void populateNeighbors(Map<Map<Property<?>, Comparable<?>>, S> states, FastMapStateHolder<S> holder) {
		if (states.size() == 1) {
			holder.setNeighborMap(ImmutableMap.of());
			holder.replacePropertyMap(Reference2ObjectMaps.emptyMap());
			return;
		}

		if (holder.getNeighborMap() != null) throw new IllegalStateException();

		else if (states == LAST_STATE_MAP.get()) holder.setStateMap((FastMap<S>) LAST_FAST_STATE_MAP.get());

		else {
			LAST_STATE_MAP.set(states);
			FastMap<S> globalTable = new FastMap<>(holder.getVanillaPropertyMap().keySet(), states, isEnabled(MemoryLeakFixMod.CompactFastMap));
			holder.setStateMap(globalTable);
			LAST_FAST_STATE_MAP.set(globalTable);
		}

		int index = holder.getStateMap().getIndexOf(holder.getVanillaPropertyMap());
		holder.setStateIndex(index);

		if (isEnabled(MemoryLeakFixMod.ReplacePropertyMap)) holder.replacePropertyMap(new FastMapEntryMap(holder));
		if (isEnabled(MemoryLeakFixMod.PopulateNeighborTable)) holder.setNeighborMap(new FastmapNeighborMap<>(holder));
		else holder.setNeighborMap(CrashNeighborMap.getInstance());
	}

	private static boolean isEnabled(boolean module) {
		return module; // MemoryLeakFixMod.isEnabled() && module;
	}

}