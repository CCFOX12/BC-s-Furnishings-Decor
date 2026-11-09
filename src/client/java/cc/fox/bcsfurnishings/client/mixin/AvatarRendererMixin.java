package cc.fox.bcsfurnishings.client.mixin;

import cc.fox.bcsfurnishings.client.doll.DollClientData;
import cc.fox.bcsfurnishings.doll.DollTags;
import io.github.apace100.cosmetic_armor.CosmeticArmor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public class AvatarRendererMixin {
	@Inject(
			method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
			at = @At("TAIL")
	)
	private void bcsfurnishings$extractHeadDoll(Avatar entity, AvatarRenderState state, float partialTick, CallbackInfo ci) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.player == entity
				&& minecraft.options.getCameraType().isFirstPerson()
				&& !(minecraft.screen instanceof AbstractContainerScreen<?>)) {
			state.setData(DollClientData.HIDE_HEAD_DOLL, Boolean.TRUE);
		}
		ItemStack cosmetic = CosmeticArmor.getCosmeticArmor(entity, EquipmentSlot.HEAD);
		if (!cosmetic.is(DollTags.ITEMS)) {
			return;
		}
		state.setData(DollClientData.HEAD_DOLL, cosmetic.copy());
		ItemStackRenderState dollState = new ItemStackRenderState();
		minecraft.getItemModelResolver().updateForLiving(dollState, cosmetic, ItemDisplayContext.HEAD, entity);
		state.setData(DollClientData.HEAD_DOLL_STATE, dollState);
		state.headItem.clear();
		state.wornHeadType = null;
	}
}
