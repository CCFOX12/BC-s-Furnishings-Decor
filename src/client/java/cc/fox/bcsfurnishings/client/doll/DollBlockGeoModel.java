package cc.fox.bcsfurnishings.client.doll;

import cc.fox.bcsfurnishings.BcsFurnishings;
import cc.fox.bcsfurnishings.doll.DollBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class DollBlockGeoModel extends DefaultedBlockGeoModel<DollBlockEntity> {
	public DollBlockGeoModel() {
		super(BcsFurnishings.id("doll"));
		withAltAnimations(BcsFurnishings.id("doll_empty"));
	}

	@Override
	public Identifier getModelResource(GeoRenderState renderState) {
		return assetId(renderState).withPrefix("block/");
	}

	@Override
	public Identifier getTextureResource(GeoRenderState renderState) {
		Identifier id = assetId(renderState);
		return Identifier.fromNamespaceAndPath(id.getNamespace(), "textures/block/" + id.getPath() + ".png");
	}

	private static Identifier assetId(GeoRenderState renderState) {
		BlockState state = renderState.getGeckolibData(DataTickets.BLOCKSTATE);
		return BuiltInRegistries.BLOCK.getKey(state.getBlock());
	}
}
