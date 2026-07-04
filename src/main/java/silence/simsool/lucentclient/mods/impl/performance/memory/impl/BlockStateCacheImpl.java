package silence.simsool.lucentclient.mods.impl.performance.memory.impl;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.google.common.base.Suppliers;

import it.unimi.dsi.fastutil.booleans.BooleanArrays;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.ArrayVoxelShape;
import net.minecraft.world.phys.shapes.VoxelShape;
import silence.simsool.lucentclient.ducks.memoryleak.BlockStateCacheAccess;
import silence.simsool.lucentclient.mixin.accessors.memoryleak.ArrayVSAccess;
import silence.simsool.lucentclient.mixin.accessors.memoryleak.SliceShapeAccess;
import silence.simsool.lucentclient.mods.impl.performance.memory.MemoryLeakFixMod;
import silence.simsool.lucentclient.mods.impl.performance.memory.impl.hash.ArrayVoxelShapeHash;
import silence.simsool.lucentclient.mods.impl.performance.memory.impl.hash.VoxelShapeHash;

public class BlockStateCacheImpl {

	public static final Map<ArrayVSAccess, VoxelShape> CACHE_COLLIDE;
	public static final Map<boolean[], boolean[]> CACHE_FACE_STURDY;
	private static final Supplier<Function<BlockBehaviour.BlockStateBase, Object>> GET_CACHE;
	private static final ThreadLocal<BlockStateCacheAccess> LAST_CACHE;

	static {
		CACHE_COLLIDE = new Object2ObjectOpenCustomHashMap<>(ArrayVoxelShapeHash.INSTANCE);
		CACHE_FACE_STURDY = new Object2ObjectOpenCustomHashMap<>(BooleanArrays.HASH_STRATEGY);
		GET_CACHE = Suppliers.memoize(() -> {
			try {
				String cacheName = MemoryLeakFixMod.PLATFORM_HOOKS.computeBlockstateCacheFieldName();
				Field cacheField = BlockBehaviour.BlockStateBase.class.getDeclaredField(cacheName);
				cacheField.setAccessible(true);
				MethodHandle getter = MethodHandles.lookup().unreflectGetter(cacheField);
				return (state) -> {
					try {
						return getter.invoke(state);
					} catch (Throwable var3) {
						throw new RuntimeException(var3);
					}
				};
			} catch (IllegalAccessException | NoSuchFieldException var3) {
				throw new RuntimeException(var3);
			}
		});
		LAST_CACHE = new ThreadLocal<>();
	}

	public static void deduplicateCachePre(BlockBehaviour.BlockStateBase state) {
		LAST_CACHE.set((BlockStateCacheAccess) GET_CACHE.get().apply(state));
	}

	public static void deduplicateCachePost(BlockBehaviour.BlockStateBase state) {
		BlockStateCacheAccess newCache = (BlockStateCacheAccess) GET_CACHE.get().apply(state);
		if (newCache != null) {
			BlockStateCacheAccess oldCache = LAST_CACHE.get();
			deduplicateCollisionShape(newCache, oldCache);
			deduplicateFaceSturdyArray(newCache, oldCache);
			LAST_CACHE.remove();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void deduplicateCollisionShape(BlockStateCacheAccess newCache, @Nullable BlockStateCacheAccess oldCache) {
		VoxelShape dedupedCollisionShape;
		if (oldCache != null && VoxelShapeHash.INSTANCE.equals(oldCache.getCollisionShape(), newCache.getCollisionShape())) {
			dedupedCollisionShape = oldCache.getCollisionShape();
		}
		else {
			dedupedCollisionShape = newCache.getCollisionShape();
			if (dedupedCollisionShape instanceof ArrayVSAccess access) {
				dedupedCollisionShape = CACHE_COLLIDE.computeIfAbsent(access, (Function) Function.identity());
			}
		}

		replaceInternals(dedupedCollisionShape, newCache.getCollisionShape());
		newCache.setCollisionShape(dedupedCollisionShape);
	}

	private static void deduplicateFaceSturdyArray(BlockStateCacheAccess newCache, @Nullable BlockStateCacheAccess oldCache) {
		boolean[] dedupedFaceSturdy;
		if (oldCache != null && Arrays.equals(oldCache.getFaceSturdy(), newCache.getFaceSturdy())) {
			dedupedFaceSturdy = oldCache.getFaceSturdy();
		} else {
			dedupedFaceSturdy = CACHE_FACE_STURDY.computeIfAbsent(newCache.getFaceSturdy(), Function.identity());
		}

		newCache.setFaceSturdy(dedupedFaceSturdy);
	}

	private static void replaceInternals(VoxelShape toKeep, VoxelShape toReplace) {
		if (toKeep instanceof ArrayVoxelShape keepArray) {
			if (toReplace instanceof ArrayVoxelShape replaceArray) {
				replaceInternals(keepArray, replaceArray);
			}
		}
	}

	public static void replaceInternals(ArrayVoxelShape toKeep, ArrayVoxelShape toReplace) {
		if (toKeep != toReplace) {
			ArrayVSAccess toReplaceAccess = (ArrayVSAccess) toReplace;
			ArrayVSAccess toKeepAccess = (ArrayVSAccess) toKeep;
			toReplaceAccess.setXPoints(toKeepAccess.getXPoints());
			toReplaceAccess.setYPoints(toKeepAccess.getYPoints());
			toReplaceAccess.setZPoints(toKeepAccess.getZPoints());
			toReplaceAccess.setFaces(toKeepAccess.getFaces());
			toReplaceAccess.setShape(toKeepAccess.getShape());
		}
	}

	private static @Nullable VoxelShape getRenderShape(@Nullable VoxelShape[] projected) {
		if (projected != null) {
			for (VoxelShape side : projected) {
				if (side instanceof SliceShapeAccess slice) {
					return slice.getDelegate();
				}
			}
		}
		return null;
	}

}