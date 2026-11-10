package cc.fox.bcsfurnishings.fashion;

import java.util.List;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public final class FashionCatalog {
	public static final List<FashionEntry> ENTRIES = List.of(
			entry("sweater_red_top", FashionKind.TEXTURE, EquipmentSlot.CHEST, false),
			entry("clown_costume_set", FashionKind.TEXTURE, EquipmentSlot.CHEST, true),
			entry("halloween_costume_set", FashionKind.TEXTURE, EquipmentSlot.CHEST, true),
			entry("skull_costume_1_set", FashionKind.TEXTURE, EquipmentSlot.CHEST, true)
	);

	private FashionCatalog() {
	}

	public static FashionEntry of(ItemStack stack) {
		if (stack.getItem() instanceof FashionItem item) {
			return item.entry();
		}
		return null;
	}

	public static boolean isFullSet(ItemStack stack) {
		FashionEntry entry = of(stack);
		return entry != null && entry.fullSet();
	}

	private static FashionEntry entry(String id, FashionKind kind, EquipmentSlot slot, boolean fullSet) {
		return new FashionEntry(id, kind, slot, fullSet);
	}
}
