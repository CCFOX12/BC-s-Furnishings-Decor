package cc.fox.bcsfurnishings.client.doll;

import cc.fox.bcsfurnishings.doll.DollBlockEntity;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class DollBlockRenderer<R extends BlockEntityRenderState & GeoRenderState> extends GeoBlockRenderer<DollBlockEntity, R> {
	public DollBlockRenderer() {
		super(new DollBlockGeoModel());
		withRenderLayer(new AlittleBirdSitGlowLayer<>(this));
	}
}
