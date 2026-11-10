package cc.fox.bcsfurnishings.recipe;

import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;

public final class DollIngredients {
	private DollIngredients() {
	}

	public static void register() {
		CustomIngredientSerializer.register(PlayerNamedHeadIngredient.SERIALIZER);
		CustomIngredientSerializer.register(FireworkFlightIngredient.SERIALIZER);
	}
}
