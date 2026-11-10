package cc.fox.bcsfurnishings.recipe;

import cc.fox.bcsfurnishings.BcsFurnishings;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.stream.Stream;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredient;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.item.crafting.display.SlotDisplay;

public record FireworkFlightIngredient(int flightDuration) implements CustomIngredient {
	public static final CustomIngredientSerializer<FireworkFlightIngredient> SERIALIZER = new Serializer();

	@Override
	public boolean test(ItemStack stack) {
		if (!stack.is(Items.FIREWORK_ROCKET)) {
			return false;
		}
		Fireworks fireworks = stack.get(DataComponents.FIREWORKS);
		return fireworks != null && fireworks.flightDuration() == this.flightDuration;
	}

	@Override
	public Stream<Holder<Item>> getMatchingItems() {
		return Stream.of(BuiltInRegistries.ITEM.wrapAsHolder(Items.FIREWORK_ROCKET));
	}

	@Override
	public SlotDisplay toDisplay() {
		ItemStack stack = new ItemStack(Items.FIREWORK_ROCKET);
		stack.set(DataComponents.FIREWORKS, new Fireworks(this.flightDuration, List.of()));
		return new SlotDisplay.ItemStackSlotDisplay(stack);
	}

	@Override
	public boolean requiresTesting() {
		return true;
	}

	@Override
	public CustomIngredientSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	private static final class Serializer implements CustomIngredientSerializer<FireworkFlightIngredient> {
		private static final Identifier ID = BcsFurnishings.id("firework_rocket");
		private static final MapCodec<FireworkFlightIngredient> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.INT.fieldOf("flight_duration").forGetter(FireworkFlightIngredient::flightDuration)
		).apply(instance, FireworkFlightIngredient::new));
		private static final StreamCodec<RegistryFriendlyByteBuf, FireworkFlightIngredient> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.VAR_INT,
				FireworkFlightIngredient::flightDuration,
				FireworkFlightIngredient::new
		);

		@Override
		public Identifier getIdentifier() {
			return ID;
		}

		@Override
		public MapCodec<FireworkFlightIngredient> getCodec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, FireworkFlightIngredient> getPacketCodec() {
			return STREAM_CODEC;
		}
	}
}
