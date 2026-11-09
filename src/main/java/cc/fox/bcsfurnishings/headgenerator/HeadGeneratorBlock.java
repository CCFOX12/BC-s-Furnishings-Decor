package cc.fox.bcsfurnishings.headgenerator;

import com.mojang.authlib.GameProfile;
import com.mojang.serialization.MapCodec;
import java.util.Optional;
import java.util.regex.Pattern;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class HeadGeneratorBlock extends HorizontalDirectionalBlock implements EntityBlock {
	public static final MapCodec<HeadGeneratorBlock> CODEC = simpleCodec(HeadGeneratorBlock::new);
	public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
	private static final Pattern PLAYER_NAME = Pattern.compile("^[A-Za-z0-9_]{1,16}$");
	private static final int TRIGGER_DELAY = 4;
	private static final double DISPENSE_OFFSET = 0.7;

	public HeadGeneratorBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(FACING, Direction.NORTH)
				.setValue(TRIGGERED, false));
	}

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, TRIGGERED);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
		return new ItemStack(this);
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new HeadGeneratorBlockEntity(pos, state);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (!canConfigure(player)) {
			return InteractionResult.PASS;
		}
		if (player instanceof ServerPlayer serverPlayer
				&& level.getBlockEntity(pos) instanceof HeadGeneratorBlockEntity blockEntity) {
			ServerPlayNetworking.send(serverPlayer, new HeadGeneratorOpenPayload(pos, blockEntity.getPlayerName()));
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
		if (!oldState.is(state.getBlock())) {
			this.updateTrigger(state, level, pos);
		}
	}

	@Override
	protected void neighborChanged(
			BlockState state,
			Level level,
			BlockPos pos,
			Block neighbor,
			@Nullable Orientation orientation,
			boolean movedByPiston
	) {
		this.updateTrigger(state, level, pos);
	}

	@Override
	protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		this.dispenseFrom(level, state, pos);
	}

	static boolean canConfigure(Player player) {
		return player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER);
	}

	static boolean acceptNameUpdate(ServerPlayer player, BlockPos pos) {
		if (!canConfigure(player) || player.level() == null) {
			return false;
		}
		if (!(player.level().getBlockEntity(pos) instanceof HeadGeneratorBlockEntity blockEntity)) {
			return false;
		}
		if (!(player.level().getBlockState(pos).getBlock() instanceof HeadGeneratorBlock)) {
			return false;
		}
		return Container.stillValidBlockEntity(blockEntity, player);
	}

	private void updateTrigger(BlockState state, Level level, BlockPos pos) {
		boolean powered = level.hasNeighborSignal(pos);
		boolean triggered = state.getValue(TRIGGERED);
		if (powered && !triggered) {
			level.scheduleTick(pos, this, TRIGGER_DELAY);
			level.setBlock(pos, state.setValue(TRIGGERED, true), Block.UPDATE_CLIENTS);
		} else if (!powered && triggered) {
			level.setBlock(pos, state.setValue(TRIGGERED, false), Block.UPDATE_CLIENTS);
		}
	}

	private void dispenseFrom(ServerLevel level, BlockState state, BlockPos pos) {
		if (!(level.getBlockEntity(pos) instanceof HeadGeneratorBlockEntity blockEntity)) {
			fail(level, pos);
			return;
		}
		String name = blockEntity.getPlayerName();
		if (!PLAYER_NAME.matcher(name).matches()) {
			fail(level, pos);
			return;
		}
		Optional<GameProfile> profile;
		try {
			profile = level.getServer().services().profileResolver().fetchByName(name);
		} catch (RuntimeException exception) {
			fail(level, pos);
			return;
		}
		if (profile.isEmpty()) {
			fail(level, pos);
			return;
		}
		ItemStack head = new ItemStack(Items.PLAYER_HEAD);
		head.set(DataComponents.PROFILE, ResolvableProfile.createResolved(profile.get()));
		Direction facing = state.getValue(FACING);
		Vec3 spawnAt = pos.getCenter().add(
				facing.getStepX() * DISPENSE_OFFSET,
				facing.getStepY() * DISPENSE_OFFSET,
				facing.getStepZ() * DISPENSE_OFFSET
		);
		DefaultDispenseItemBehavior.spawnItem(level, head, 6, facing, spawnAt);
		level.levelEvent(LevelEvent.SOUND_DISPENSER_DISPENSE, pos, 0);
		level.levelEvent(LevelEvent.PARTICLES_SHOOT_SMOKE, pos, facing.get3DDataValue());
	}

	private static void fail(ServerLevel level, BlockPos pos) {
		level.levelEvent(LevelEvent.SOUND_DISPENSER_FAIL, pos, 0);
	}
}
