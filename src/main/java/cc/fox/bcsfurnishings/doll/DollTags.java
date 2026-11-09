package cc.fox.bcsfurnishings.doll;

import cc.fox.bcsfurnishings.BcsFurnishings;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class DollTags {
	public static final TagKey<Item> ITEMS = TagKey.create(Registries.ITEM, BcsFurnishings.id("dolls"));
	public static final TagKey<Block> BLOCKS = TagKey.create(Registries.BLOCK, BcsFurnishings.id("dolls"));

	private DollTags() {
	}
}
