package cc.fox.bcsfurnishings.client.doll;

import cc.fox.bcsfurnishings.doll.DollBlocks;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;

public final class DollClientHooks {
	private DollClientHooks() {
	}

	public static void register() {
		BlockEntityRenderers.register(DollBlocks.BLOCK_ENTITY, context -> new DollBlockRenderer<>());
		LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, renderer, helper, context) -> {
			if (renderer instanceof AvatarRenderer<?> avatarRenderer) {
				helper.register(new DollHeadFeatureRenderer(avatarRenderer));
			}
		});
	}
}
