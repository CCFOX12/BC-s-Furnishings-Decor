package cc.fox.bcsfurnishings.client.jade;

import cc.fox.bcsfurnishings.doll.DollBlock;
import cc.fox.bcsfurnishings.headgenerator.HeadGeneratorBlock;
import snownee.jade.api.IWailaClientRegistration;

public final class DollJadeClient {
	private DollJadeClient() {
	}

	public static void register(IWailaClientRegistration registration) {
		registration.registerBlockComponent(DollJadeProvider.INSTANCE, DollBlock.class);
		registration.registerBlockComponent(HeadGeneratorJadeProvider.INSTANCE, HeadGeneratorBlock.class);
	}
}
