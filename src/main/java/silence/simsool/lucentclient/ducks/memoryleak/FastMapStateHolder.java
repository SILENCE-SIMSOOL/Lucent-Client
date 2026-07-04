package silence.simsool.lucentclient.ducks.memoryleak;

import java.util.Map;

import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import net.minecraft.world.level.block.state.properties.Property;
import silence.simsool.lucentclient.mods.impl.performance.memory.impl.fastmap.FastMap;

public interface FastMapStateHolder<S> {
    FastMap<S> getStateMap();

    void setStateMap(FastMap<S> newValue);

    int getStateIndex();

    void setStateIndex(int newValue);

    Reference2ObjectMap<Property<?>, Comparable<?>> getVanillaPropertyMap();

    void replacePropertyMap(Reference2ObjectMap<Property<?>, Comparable<?>> newMap);

    void setNeighborMap(Map<Property<?>, S[]> table);

    Map<Property<?>, S[]> getNeighborMap();
}