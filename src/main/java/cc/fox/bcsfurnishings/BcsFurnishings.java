package cc.fox.bcsfurnishings;

import cc.fox.bcsfurnishings.doll.DollBlocks;
import cc.fox.bcsfurnishings.fashion.FashionItems;
import cc.fox.bcsfurnishings.headgenerator.HeadGeneratorBlocks;
import cc.fox.bcsfurnishings.recipe.DollIngredients;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BcsFurnishings implements ModInitializer {
	public static final String MOD_ID = "bcs_furnishings";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		DollIngredients.register();
		DollBlocks.register();
		FashionItems.register();
		HeadGeneratorBlocks.register();
		LOGGER.info("BC's Furnishings & Decor loaded {} dolls, {} fashion items",
				DollBlocks.items().size(), FashionItems.items().size());
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
