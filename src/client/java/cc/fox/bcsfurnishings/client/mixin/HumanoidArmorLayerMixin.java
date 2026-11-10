package cc.fox.bcsfurnishings.client.mixin;

import cc.fox.bcsfurnishings.fashion.FashionTags;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin {
	@Inject(
			method = "shouldRender(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlot;)Z",
			at = @At("HEAD"),
			cancellable = true
	)
	private static void bcsfurnishings$skipFashionArmor(
			ItemStack stack, EquipmentSlot slot, CallbackInfoReturnable<Boolean> cir
	) {
		if (stack.is(FashionTags.ITEMS)) {
			cir.setReturnValue(false);
		}
	}
}
