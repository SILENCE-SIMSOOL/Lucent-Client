package silence.simsool.lucentclient.mixin.mixins.packmanager;

import java.util.List;
import java.util.function.Function;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import silence.simsool.lucentclient.mods.impl.utility.packmanager.PackManagerMod;

@Mixin(Pack.Position.class)
public class MixinPackPosition {

	@Inject(method = "insert", at = @At("HEAD"), cancellable = true)
	public <T> void insert(List<T> items, T item, Function<T, PackSelectionConfig> profileGetter, boolean listInverted, CallbackInfoReturnable<Integer> cir) {
		if (PackManagerMod.isEnabled() && PackManagerMod.ServerUnlocker) {
			if (((Pack) item).getPackSource() == PackSource.SERVER) {
				int i;
				for (i = 0; i < items.size(); i++) {
					Pack pack = (Pack) items.get(i);
					if (!pack.isFixedPosition() || pack.getDefaultPosition() != Pack.Position.TOP) {
						break;
					}
				}
				i += 1;
				items.add(i, item);
				cir.setReturnValue(i);
			}
		}
	}

}