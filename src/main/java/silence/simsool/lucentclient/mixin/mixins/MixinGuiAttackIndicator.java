package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.gui.Gui;

@Mixin(Gui.class)
public class MixinGuiAttackIndicator {
	
	// Default vanilla attack indicator rendering is usually in renderCrosshair or a separate method.
	// But according to the options: Block Breaking, Ranged Weapon Draw, Eating and Drinking, Item Cooldowns.
	// We might need to cancel these specific indicators. 
	// Or maybe the indicator rendering logic uses shouldDrawSurvivalElements?
}
