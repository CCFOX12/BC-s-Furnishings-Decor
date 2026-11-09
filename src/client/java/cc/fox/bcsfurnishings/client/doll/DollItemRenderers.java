package cc.fox.bcsfurnishings.client.doll;

import cc.fox.bcsfurnishings.doll.DollBlockItem;
import java.util.function.Consumer;
import org.jspecify.annotations.Nullable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public final class DollItemRenderers {
	private DollItemRenderers() {
	}

	public static void accept(DollBlockItem item, Consumer<GeoRenderProvider> consumer) {
		consumer.accept(new GeoRenderProvider() {
			private final GeoItemRenderer<DollBlockItem> renderer = new DollItemRenderer(item);

			@Override
			public @Nullable GeoItemRenderer<?> getGeoItemRenderer() {
				return this.renderer;
			}
		});
	}
}
