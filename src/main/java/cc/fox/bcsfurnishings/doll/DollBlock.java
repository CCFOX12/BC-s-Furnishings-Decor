package cc.fox.bcsfurnishings.doll;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class DollBlock extends HorizontalDirectionalBlock implements EntityBlock {
	public static final MapCodec<DollBlock> CODEC = simpleCodec(DollBlock::new);
	public static final BooleanProperty LIT = BlockStateProperties.LIT;
	public static final String GLOW_LAMP_ID = "alittle_bird_sit";
	public static final int GLOW_LAMP_LIGHT = 15;
	private static final VoxelShape SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 10.0, 13.0);

	private final boolean glowLamp;

	public DollBlock(Properties properties) {
		this(properties, false);
	}

	public DollBlock(Properties properties, boolean glowLamp) {
		super(properties);
		this.glowLamp = glowLamp;
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(FACING, Direction.NORTH)
				.setValue(LIT, true));
	}

	public boolean isGlowLamp() {
		return this.glowLamp;
	}

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, LIT);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new DollBlockEntity(pos, state);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (!this.glowLamp) {
			return InteractionResult.PASS;
		}
		return toggleLamp(state, level, pos, player);
	}

	@Override
	protected InteractionResult useItemOn(
			ItemStack stack,
			BlockState state,
			Level level,
			BlockPos pos,
			Player player,
			InteractionHand hand,
			BlockHitResult hit
	) {
		if (!this.glowLamp || player.isSecondaryUseActive()) {
			return super.useItemOn(stack, state, level, pos, player, hand, hit);
		}
		return toggleLamp(state, level, pos, player);
	}

	private static InteractionResult toggleLamp(BlockState state, Level level, BlockPos pos, Player player) {
		BlockState next = state.cycle(LIT);
		level.setBlock(pos, next, Block.UPDATE_ALL);
		level.playSound(
				player,
				pos,
				next.getValue(LIT) ? SoundEvents.COPPER_BULB_TURN_ON : SoundEvents.COPPER_BULB_TURN_OFF,
				SoundSource.BLOCKS,
				1.0F,
				1.0F
		);
		return InteractionResult.SUCCESS;
	}
}
