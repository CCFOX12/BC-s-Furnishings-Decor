package cc.fox.bcsfurnishings.recipe;

import cc.fox.bcsfurnishings.BcsFurnishings;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.crafting.display.SlotDisplay;

public record PlayerNamedHeadIngredient(String name) implements CustomIngredient {
	public static final CustomIngredientSerializer<PlayerNamedHeadIngredient> SERIALIZER = new Serializer();

	@Override
	public boolean test(ItemStack stack) {
		if (!stack.is(Items.PLAYER_HEAD)) {
			return false;
		}
		ResolvableProfile profile = stack.get(DataComponents.PROFILE);
		return profile != null && profile.name().filter(this::nameEquals).isPresent();
	}

	private boolean nameEquals(String actual) {
		return this.name.equalsIgnoreCase(actual);
	}

	@Override
	public Stream<Holder<Item>> getMatchingItems() {
		return Stream.of(BuiltInRegistries.ITEM.wrapAsHolder(Items.PLAYER_HEAD));
	}

	@Override
	public SlotDisplay toDisplay() {
		ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
		stack.set(DataComponents.PROFILE, ResolvableProfile.createUnresolved(this.name));
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

	private static final class Serializer implements CustomIngredientSerializer<PlayerNamedHeadIngredient> {
		private static final Identifier ID = BcsFurnishings.id("player_head");
		private static final MapCodec<PlayerNamedHeadIngredient> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.STRING.fieldOf("name").forGetter(PlayerNamedHeadIngredient::name)
		).apply(instance, PlayerNamedHeadIngredient::new));
		private static final StreamCodec<RegistryFriendlyByteBuf, PlayerNamedHeadIngredient> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.STRING_UTF8,
				PlayerNamedHeadIngredient::name,
				PlayerNamedHeadIngredient::new
		);

		@Override
		public Identifier getIdentifier() {
			return ID;
		}

		@Override
		public MapCodec<PlayerNamedHeadIngredient> getCodec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, PlayerNamedHeadIngredient> getPacketCodec() {
			return STREAM_CODEC;
		}
	}
}
