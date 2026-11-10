package cc.fox.bcsfurnishings.client.mixin;

import cc.fox.bcsfurnishings.doll.DollTags;
import cc.fox.bcsfurnishings.fashion.FashionTags;
import io.github.apace100.cosmetic_armor.CosmeticArmor;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
	@Inject(
			method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V",
			at = @At("TAIL")
	)
	private void bcsfurnishings$skipDollHeadItem(LivingEntity entity, LivingEntityRenderState state, float partialTick, CallbackInfo ci) {
		ItemStack realHead = entity.getItemBySlot(EquipmentSlot.HEAD);
		ItemStack cosmeticHead = CosmeticArmor.getCosmeticArmor(entity, EquipmentSlot.HEAD);
		if (realHead.is(DollTags.ITEMS) || realHead.is(FashionTags.ITEMS)
				|| cosmeticHead.is(DollTags.ITEMS) || cosmeticHead.is(FashionTags.ITEMS)) {
			state.headItem.clear();
			state.wornHeadType = null;
		}
	}
}
