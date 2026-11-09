package cc.fox.bcsfurnishings.headgenerator;

import cc.fox.bcsfurnishings.BcsFurnishings;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record HeadGeneratorSetNamePayload(BlockPos pos, String name) implements CustomPacketPayload {
	public static final Type<HeadGeneratorSetNamePayload> TYPE = new Type<>(BcsFurnishings.id("head_generator_set_name"));
	public static final StreamCodec<RegistryFriendlyByteBuf, HeadGeneratorSetNamePayload> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC,
			HeadGeneratorSetNamePayload::pos,
			ByteBufCodecs.STRING_UTF8,
			HeadGeneratorSetNamePayload::name,
			HeadGeneratorSetNamePayload::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
