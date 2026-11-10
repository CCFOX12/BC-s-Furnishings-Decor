package cc.fox.bcsfurnishings.fashion;

import dev.emi.trinkets.api.TrinketInventory;
import dev.emi.trinkets.api.TrinketsApi;
import java.util.Map;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class FashionSlots {
	private static final EquipmentSlot[] MIX_SLOTS = {
			EquipmentSlot.HEAD, EquipmentSlot.LEGS, EquipmentSlot.FEET
	};

	private FashionSlots() {
	}

	public static TrinketInventory cosmetic(LivingEntity entity, EquipmentSlot slot) {
		return TrinketsApi.getTrinketComponent(entity)
				.map(component -> {
					Map<String, TrinketInventory> group = component.getInventory().get(slot.getName());
					if (group == null) {
						return null;
					}
					TrinketInventory cosmetic = group.get("cosmetic");
					if (cosmetic == null || cosmetic.getContainerSize() < 1) {
						return null;
					}
					return cosmetic;
				})
				.orElse(null);
	}

	public static boolean hasFullSet(LivingEntity entity) {
		TrinketInventory chest = cosmetic(entity, EquipmentSlot.CHEST);
		return chest != null && FashionCatalog.isFullSet(chest.getItem(0));
	}

	public static boolean canWearWithCurrentSet(LivingEntity entity, FashionEntry entry) {
		if (entry.fullSet() || entry.slot() == EquipmentSlot.CHEST) {
			return true;
		}
		return !hasFullSet(entity);
	}

	public static void ejectMixFashion(Player player) {
		for (EquipmentSlot slot : MIX_SLOTS) {
			TrinketInventory inventory = cosmetic(player, slot);
			if (inventory == null) {
				continue;
			}
			ItemStack worn = inventory.getItem(0);
			if (worn.isEmpty() || !worn.is(FashionTags.ITEMS)) {
				continue;
			}
			inventory.setItem(0, ItemStack.EMPTY);
			inventory.markUpdate();
			player.getInventory().placeItemBackInInventory(worn);
		}
	}
}
