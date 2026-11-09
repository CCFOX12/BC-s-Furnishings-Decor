package cc.fox.bcsfurnishings.headgenerator;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public class HeadGeneratorBlockEntity extends BlockEntity {
	private String playerName = "";

	public HeadGeneratorBlockEntity(BlockPos pos, BlockState state) {
		super(HeadGeneratorBlocks.BLOCK_ENTITY, pos, state);
	}

	public String getPlayerName() {
		return this.playerName;
	}

	public void setPlayerName(String name) {
		String trimmed = name == null ? "" : name.trim();
		if (trimmed.length() > 16) {
			trimmed = trimmed.substring(0, 16);
		}
		if (this.playerName.equals(trimmed)) {
			return;
		}
		this.playerName = trimmed;
		this.setChanged();
		if (this.level != null && !this.level.isClientSide()) {
			this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
		}
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		output.putString("PlayerName", this.playerName);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.playerName = input.getStringOr("PlayerName", "");
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		return this.saveWithoutMetadata(registries);
	}

	@Override
	public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
}
