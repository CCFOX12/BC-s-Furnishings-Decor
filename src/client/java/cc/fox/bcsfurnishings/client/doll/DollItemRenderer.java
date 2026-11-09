package cc.fox.bcsfurnishings.client.doll;

import cc.fox.bcsfurnishings.BcsFurnishings;
import cc.fox.bcsfurnishings.doll.DollBlockItem;
import net.minecraft.core.registries.BuiltInRegistries;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class DollItemRenderer extends GeoItemRenderer<DollBlockItem> {
	public DollItemRenderer(DollBlockItem item) {
		super(new DefaultedBlockGeoModel<DollBlockItem>(BuiltInRegistries.ITEM.getKey(item))
				.withAltAnimations(BcsFurnishings.id("doll_empty")));
	}
}
