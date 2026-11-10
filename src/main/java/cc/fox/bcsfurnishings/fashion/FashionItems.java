package cc.fox.bcsfurnishings.fashion;

import cc.fox.bcsfurnishings.BcsFurnishings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.equipment.Equippable;

public final class FashionItems {
	public static final ResourceKey<CreativeModeTab> TAB_KEY = ResourceKey.create(
			Registries.CREATIVE_MODE_TAB, BcsFurnishings.id("fashion"));
	private static final List<FashionItem> ITEMS = new ArrayList<>();
	private static final ItemLore COSMETIC_LORE = grayLore("tooltip.bcs_furnishings.fashion_cosmetic");

	private FashionItems() {
	}

	public static void register() {
		for (FashionEntry entry : FashionCatalog.ENTRIES) {
			if (entry.kind() != FashionKind.TEXTURE) {
				continue;
			}
			ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, entry.identifier());
			FashionItem item = Registry.register(BuiltInRegistries.ITEM, itemKey, new FashionItem(entry, new Item.Properties()
					.setId(itemKey)
					.stacksTo(64)
					.component(DataComponents.EQUIPPABLE, Equippable.builder(entry.slot())
							.setEquipSound(SoundEvents.ARMOR_EQUIP_GENERIC)
							.setDispensable(false)
							.setSwappable(false)
							.setDamageOnHurt(false)
							.build())
					.component(DataComponents.LORE, COSMETIC_LORE)
			));
			ITEMS.add(item);
		}

		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, TAB_KEY, FabricItemGroup.builder()
				.title(Component.translatable("itemGroup.bcs_furnishings.fashion"))
				.icon(() -> new ItemStack(ITEMS.getFirst()))
				.displayItems((parameters, output) -> {
					for (FashionItem item : ITEMS) {
						output.accept(item);
					}
				})
				.build());
	}

	public static List<FashionItem> items() {
		return Collections.unmodifiableList(ITEMS);
	}

	private static ItemLore grayLore(String key) {
		List<Component> lines = List.of(Component.translatable(key).withStyle(ChatFormatting.GRAY));
		return new ItemLore(lines, lines);
	}
}
