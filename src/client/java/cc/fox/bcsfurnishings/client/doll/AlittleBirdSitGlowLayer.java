package cc.fox.bcsfurnishings.client.doll;

import cc.fox.bcsfurnishings.doll.DollBlock;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.GeoRenderer;
import software.bernie.geckolib.renderer.layer.builtin.AutoGlowingGeoLayer;

/**
 * Emissive pass for glow-lamp dolls. Other dolls have no glowmask and must not
 * request {@code *_glowmask.png}.
 * <p>
 * GeckoLib's built-in {@code geckolib:pipeline/emissive} is not in Iris's pipeline map,
 * so a shader pack discards that pass. Vanilla {@link RenderTypes#eyes} uses
 * {@code RenderPipelines.EYES}, which Iris sends to {@code gbuffers_spidereyes}.
 */
public final class AlittleBirdSitGlowLayer<T extends GeoAnimatable, O, R extends GeoRenderState>
		extends AutoGlowingGeoLayer<T, O, R> {
	private static final String BASE_TEXTURE_PATH = "textures/block/alittle_bird_sit.png";

	public AlittleBirdSitGlowLayer(GeoRenderer<T, O, R> renderer) {
		super(renderer);
	}

	@Override
	protected @Nullable RenderType getRenderType(R renderState) {
		Identifier base = this.renderer.getTextureLocation(renderState);
		if (!BASE_TEXTURE_PATH.equals(base.getPath())) {
			return null;
		}

		BlockState blockState = renderState.getGeckolibData(DataTickets.BLOCKSTATE);
		if (blockState != null && blockState.hasProperty(DollBlock.LIT) && !blockState.getValue(DollBlock.LIT)) {
			return null;
		}

		return RenderTypes.eyes(getTextureResource(renderState));
	}
}
