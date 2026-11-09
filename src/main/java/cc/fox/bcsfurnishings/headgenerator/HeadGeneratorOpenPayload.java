package cc.fox.bcsfurnishings.headgenerator;

import cc.fox.bcsfurnishings.BcsFurnishings;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record HeadGeneratorOpenPayload(BlockPos pos, String name) implements CustomPacketPayload {
	public static final Type<HeadGeneratorOpenPayload> TYPE = new Type<>(BcsFurnishings.id("head_generator_open"));
	public static final StreamCodec<RegistryFriendlyByteBuf, HeadGeneratorOpenPayload> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC,
			HeadGeneratorOpenPayload::pos,
			ByteBufCodecs.STRING_UTF8,
			HeadGeneratorOpenPayload::name,
			HeadGeneratorOpenPayload::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
