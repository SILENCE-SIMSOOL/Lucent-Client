package silence.simsool.lucentclient.mixin.mixins.controlling;

import java.util.Map;
import com.mojang.blaze3d.platform.InputConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(InputConstants.Key.class)
public interface AccessInputConstantsKey {
	@Accessor("NAME_MAP")
	static Map<String, InputConstants.Key> lucent$getNAME_MAP() {
		throw new AssertionError();
	}
}