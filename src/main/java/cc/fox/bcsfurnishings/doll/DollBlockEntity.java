package cc.fox.bcsfurnishings.doll;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class DollBlockEntity extends BlockEntity implements GeoBlockEntity {
	private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

	public DollBlockEntity(BlockPos pos, BlockState state) {
		super(DollBlocks.BLOCK_ENTITY, pos, state);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(test -> PlayState.STOP));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.geoCache;
	}
}
