package cc.fox.bcsfurnishings.client.doll;

import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemStack;

public final class DollClientData {
	public static final RenderStateDataKey<ItemStack> HEAD_DOLL = RenderStateDataKey.create(() -> "bcs_furnishings:head_doll");
	public static final RenderStateDataKey<ItemStackRenderState> HEAD_DOLL_STATE = RenderStateDataKey.create(() -> "bcs_furnishings:head_doll_state");
	public static final RenderStateDataKey<Boolean> HIDE_HEAD_DOLL = RenderStateDataKey.create(() -> "bcs_furnishings:hide_head_doll");
	public static final float HEAD_SCALE = 0.625F;
	/** Head pivot is the neck; the 8px cube top is local Y=-0.5. Sit the geo feet on the crown. */
	public static final float HEAD_SIT_Y = -0.5F;

	private DollClientData() {
	}
}
