package cc.fox.bcsfurnishings.headgenerator;

import cc.fox.bcsfurnishings.BcsFurnishings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public final class HeadGeneratorBlocks {
	public static final ResourceKey<CreativeModeTab> TAB_KEY = ResourceKey.create(
			Registries.CREATIVE_MODE_TAB, BcsFurnishings.id("utility"));
	public static HeadGeneratorBlock BLOCK;
	public static HeadGeneratorBlockItem ITEM;
	public static BlockEntityType<HeadGeneratorBlockEntity> BLOCK_ENTITY;

	private HeadGeneratorBlocks() {
	}

	public static void register() {
		ResourceKey<Block> blockKey = ResourceKey.create(
				Registries.BLOCK, BcsFurnishings.id("head_generator"));
		BLOCK = Registry.register(BuiltInRegistries.BLOCK, blockKey, new HeadGeneratorBlock(
				BlockBehaviour.Properties.of()
						.setId(blockKey)
						.mapColor(MapColor.STONE)
						.sound(SoundType.STONE)
						.strength(3.5F)
						.requiresCorrectToolForDrops()
		));
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, BcsFurnishings.id("head_generator"));
		ITEM = Registry.register(BuiltInRegistries.ITEM, itemKey, new HeadGeneratorBlockItem(BLOCK, new Item.Properties()
				.setId(itemKey)
				.useBlockDescriptionPrefix()
				.stacksTo(64)
		));
		BLOCK_ENTITY = Registry.register(
				BuiltInRegistries.BLOCK_ENTITY_TYPE,
				BcsFurnishings.id("head_generator"),
				FabricBlockEntityTypeBuilder.create(HeadGeneratorBlockEntity::new, BLOCK).build()
		);

		PayloadTypeRegistry.playS2C().register(HeadGeneratorOpenPayload.TYPE, HeadGeneratorOpenPayload.STREAM_CODEC);
		PayloadTypeRegistry.playC2S().register(HeadGeneratorSetNamePayload.TYPE, HeadGeneratorSetNamePayload.STREAM_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(HeadGeneratorSetNamePayload.TYPE, (payload, context) -> {
			if (!HeadGeneratorBlock.acceptNameUpdate(context.player(), payload.pos())) {
				return;
			}
			if (context.player().level().getBlockEntity(payload.pos()) instanceof HeadGeneratorBlockEntity blockEntity) {
				blockEntity.setPlayerName(payload.name());
			}
		});

		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, TAB_KEY, FabricItemGroup.builder()
				.title(Component.translatable("itemGroup.bcs_furnishings.utility"))
				.icon(() -> new ItemStack(ITEM))
				.displayItems((parameters, output) -> output.accept(ITEM))
				.build());
	}
}
