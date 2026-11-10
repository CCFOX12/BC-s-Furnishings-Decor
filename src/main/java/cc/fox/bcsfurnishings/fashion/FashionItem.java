package cc.fox.bcsfurnishings.fashion;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketInventory;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FashionItem extends Item implements Trinket {
	private final FashionEntry entry;

	public FashionItem(FashionEntry entry, Item.Properties properties) {
		super(properties);
		this.entry = entry;
		TrinketsApi.registerTrinket(this, this);
	}

	public FashionEntry entry() {
		return this.entry;
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack held = player.getItemInHand(hand);
		TrinketInventory cosmetic = FashionSlots.cosmetic(player, this.entry.slot());
		if (cosmetic == null || held.isEmpty()) {
			return InteractionResult.FAIL;
		}
		if (!FashionSlots.canWearWithCurrentSet(player, this.entry)) {
			if (!level.isClientSide()) {
				level.playSound(null, player.getX(), player.getY(), player.getZ(),
						SoundEvents.DISPENSER_FAIL, SoundSource.PLAYERS, 1.0F, 1.0F);
			}
			return InteractionResult.FAIL;
		}
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}

		ItemStack equipped = cosmetic.getItem(0).copy();
		ItemStack toWear = player.hasInfiniteMaterials() ? held.copyWithCount(1) : held.split(1);
		cosmetic.setItem(0, toWear);
		cosmetic.markUpdate();
		if (this.entry.fullSet()) {
			FashionSlots.ejectMixFashion(player);
		}
		if (!equipped.isEmpty()) {
			if (held.isEmpty()) {
				player.setItemInHand(hand, equipped);
			} else {
				player.getInventory().placeItemBackInInventory(equipped);
			}
		}
		level.playSound(null, player.getX(), player.getY(), player.getZ(),
				SoundEvents.ARMOR_EQUIP_GENERIC, SoundSource.PLAYERS, 1.0F, 1.0F);
		return InteractionResult.SUCCESS;
	}

	@Override
	public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		if (!"cosmetic".equals(slot.inventory().getSlotType().getName())) {
			return false;
		}
		if (!this.entry.trinketGroup().equals(slot.inventory().getSlotType().getGroup())) {
			return false;
		}
		return FashionSlots.canWearWithCurrentSet(entity, this.entry);
	}

	@Override
	public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		if (entity.level().isClientSide() || !this.entry.fullSet()) {
			return;
		}
		if (entity instanceof Player player) {
			FashionSlots.ejectMixFashion(player);
		}
	}

	@Override
	public Holder<SoundEvent> getEquipSound(ItemStack stack, SlotReference slot, LivingEntity entity) {
		return SoundEvents.ARMOR_EQUIP_GENERIC;
	}
}
