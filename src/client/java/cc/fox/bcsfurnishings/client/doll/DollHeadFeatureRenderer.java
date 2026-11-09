package cc.fox.bcsfurnishings.client.doll;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class DollHeadFeatureRenderer extends RenderLayer<AvatarRenderState, PlayerModel> {
	public DollHeadFeatureRenderer(RenderLayerParent<AvatarRenderState, PlayerModel> parent) {
		super(parent);
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector renderTasks, int packedLight, AvatarRenderState state, float yRot, float xRot) {
		if (Boolean.TRUE.equals(state.getData(DollClientData.HIDE_HEAD_DOLL))) {
			return;
		}
		ItemStackRenderState dollState = state.getData(DollClientData.HEAD_DOLL_STATE);
		if (dollState == null || dollState.isEmpty()) {
			return;
		}

		PlayerModel model = this.getParentModel();
		poseStack.pushPose();
		model.root().translateAndRotate(poseStack);
		model.translateToHead(poseStack);
		poseStack.translate(0.0F, DollClientData.HEAD_SIT_Y, 0.0F);
		poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
		poseStack.scale(DollClientData.HEAD_SCALE, -DollClientData.HEAD_SCALE, -DollClientData.HEAD_SCALE);
		dollState.submit(poseStack, renderTasks, packedLight, OverlayTexture.NO_OVERLAY, state.outlineColor);
		poseStack.popPose();
	}
}
