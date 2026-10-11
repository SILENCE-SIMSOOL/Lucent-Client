package silence.simsool.lucentclient.mixin.mixins.asyncparticles.conditional;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import silence.simsool.lucentclient.features.asyncparticles.core.Diagnostic;
import silence.simsool.lucentclient.features.asyncparticles.util.ThreadUtil;
import net.minecraft.util.ClassInstanceMultiMap;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

// some mod get entities when ticking particles, may cause a CME
@Mixin(value = ClassInstanceMultiMap.class, priority = 1500) // higher priority to run after VMP's mixin
public class MixinClassInstanceMultiMap_SafeClassInstanceMultiMap_Off {
	@WrapMethod(method = "getAllInstances")
	public List<?> onGetAllInstances(Operation<List<?>> original) {
		if (!ThreadUtil.isOnParticleThread()) {
			return original.call();
		}
		Diagnostic.illegalEntityStorageAccess();
		return Collections.emptyList();
	}

	@WrapMethod(method = "iterator")
	public Iterator<?> onIterator(Operation<Iterator<?>> original) {
		if (!ThreadUtil.isOnParticleThread()) {
			return original.call();
		}
		Diagnostic.illegalEntityStorageAccess();
		return Collections.emptyIterator();
	}

	@WrapMethod(method = "find")
	public Collection<?> onFind(Class<?> class_, Operation<Collection<?>> original) {
		if (!ThreadUtil.isOnParticleThread()) {
			return original.call(class_);
		}
		Diagnostic.illegalEntityStorageAccess();
		return Collections.emptyList();
	}
}