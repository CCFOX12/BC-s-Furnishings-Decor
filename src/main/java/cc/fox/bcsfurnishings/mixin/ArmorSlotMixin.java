package cc.fox.bcsfurnishings.mixin;

import cc.fox.bcsfurnishings.doll.DollTags;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.inventory.ArmorSlot")
public class ArmorSlotMixin {
	@Inject(method = "mayPlace", at = @At("HEAD"), cancellable = true)
	private void bcsfurnishings$rejectDolls(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (stack.is(DollTags.ITEMS)) {
			cir.setReturnValue(false);
		}
	}
}
