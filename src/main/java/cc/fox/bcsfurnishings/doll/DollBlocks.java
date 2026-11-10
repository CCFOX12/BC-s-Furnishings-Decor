package cc.fox.bcsfurnishings.doll;

import cc.fox.bcsfurnishings.BcsFurnishings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public final class DollBlocks {
	public static final ResourceKey<CreativeModeTab> TAB_KEY = ResourceKey.create(
			Registries.CREATIVE_MODE_TAB, BcsFurnishings.id("dolls"));
	public static BlockEntityType<DollBlockEntity> BLOCK_ENTITY;
	private static final List<DollBlockItem> ITEMS = new ArrayList<>();
	private static final List<DollBlock> BLOCKS = new ArrayList<>();

	private DollBlocks() {
	}

	public static void register() {
		List<Block> registeredBlocks = new ArrayList<>();
		for (DollEntry entry : DollCatalog.ENTRIES) {
			ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, entry.identifier());
			boolean glowLamp = DollBlock.GLOW_LAMP_ID.equals(entry.id());
			int litLight = glowLamp ? DollBlock.GLOW_LAMP_LIGHT : 0;
			DollBlock block = Registry.register(BuiltInRegistries.BLOCK, blockKey, new DollBlock(
					BlockBehaviour.Properties.of()
							.setId(blockKey)
							.mapColor(MapColor.WOOL)
							.sound(SoundType.WOOL)
							.strength(0.8F)
							.noOcclusion()
							.lightLevel(state -> state.getValue(DollBlock.LIT) ? litLight : 0)
							.pushReaction(PushReaction.BLOCK)
							.isSuffocating((state, getter, pos) -> false)
							.isViewBlocking((state, getter, pos) -> false),
					glowLamp
			));
			ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, entry.identifier());
			Item.Properties itemProperties = new Item.Properties()
					.setId(itemKey)
					.useBlockDescriptionPrefix()
					.stacksTo(64)
					.component(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.HEAD)
							.setDamageOnHurt(false)
							.build())
					.component(DataComponents.LORE, glowLamp
							? grayLore("tooltip.bcs_furnishings.cosmetic_head", "tooltip.bcs_furnishings.glow_lamp")
							: grayLore("tooltip.bcs_furnishings.cosmetic_head"));
			DollBlockItem item = Registry.register(BuiltInRegistries.ITEM, itemKey, new DollBlockItem(block, itemProperties));
			BLOCKS.add(block);
			ITEMS.add(item);
			registeredBlocks.add(block);
		}

		BLOCK_ENTITY = Registry.register(
				BuiltInRegistries.BLOCK_ENTITY_TYPE,
				BcsFurnishings.id("doll"),
				FabricBlockEntityTypeBuilder.create(DollBlockEntity::new, registeredBlocks.toArray(Block[]::new)).build()
		);

		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, TAB_KEY, FabricItemGroup.builder()
				.title(Component.translatable("itemGroup.bcs_furnishings.dolls"))
				.icon(() -> new ItemStack(ITEMS.getFirst()))
				.displayItems((parameters, output) -> {
					for (DollBlockItem item : ITEMS) {
						output.accept(item);
					}
				})
				.build());
	}

	public static List<DollBlockItem> items() {
		return Collections.unmodifiableList(ITEMS);
	}

	public static List<DollBlock> blocks() {
		return Collections.unmodifiableList(BLOCKS);
	}

	private static ItemLore grayLore(String... keys) {
		List<Component> lines = new ArrayList<>(keys.length);
		for (String key : keys) {
			lines.add(Component.translatable(key).withStyle(ChatFormatting.GRAY));
		}
		List<Component> frozen = List.copyOf(lines);
		return new ItemLore(frozen, frozen);
	}
}
