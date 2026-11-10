package cc.fox.bcsfurnishings.client.fashion;

import cc.fox.bcsfurnishings.BcsFurnishings;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public final class FashionClientHooks {
	private FashionClientHooks() {
	}

	public static void register() {
		ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(
				BcsFurnishings.id("fashion_skins"),
				(ResourceManagerReloadListener) manager -> FashionSkinCache.clear()
		);
	}
}
