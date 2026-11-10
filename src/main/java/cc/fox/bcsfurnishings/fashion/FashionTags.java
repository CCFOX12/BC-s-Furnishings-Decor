package cc.fox.bcsfurnishings.fashion;

import cc.fox.bcsfurnishings.BcsFurnishings;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class FashionTags {
	public static final TagKey<Item> ITEMS = TagKey.create(Registries.ITEM, BcsFurnishings.id("fashion"));

	private FashionTags() {
	}
}
