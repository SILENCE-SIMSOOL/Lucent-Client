package silence.simsool.lucentclient.mixin.mixins.memoryleak;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import it.unimi.dsi.fastutil.longs.Long2FloatLinkedOpenHashMap;
import net.minecraft.world.level.biome.Biome;
import silence.simsool.lucentclient.mods.impl.performance.MemoryLeakFixMod;

@Mixin(Biome.class)
public abstract class MixinBiome_ThreadLocal {

	@Unique
	private static ThreadLocal<Long2FloatLinkedOpenHashMap> betterTempCache;

	@SuppressWarnings("unchecked")
	@WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/lang/ThreadLocal;withInitial(Ljava/util/function/Supplier;)Ljava/lang/ThreadLocal;"))
	private ThreadLocal<Long2FloatLinkedOpenHashMap> useStaticThreadLocal(Supplier<?> supplier, Operation<ThreadLocal<Long2FloatLinkedOpenHashMap>> original) {
		if (MemoryLeakFixMod.isEnabled() && MemoryLeakFixMod.StaticBiomeCache) {
			if (betterTempCache == null) betterTempCache = original.call((Supplier<Long2FloatLinkedOpenHashMap>) supplier);
			return betterTempCache;
		}
		return original.call((Supplier<Long2FloatLinkedOpenHashMap>) supplier);
	}

}