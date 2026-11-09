package cc.fox.bcsfurnishings.client;

import cc.fox.bcsfurnishings.client.doll.DollClientHooks;
import cc.fox.bcsfurnishings.client.headgenerator.HeadGeneratorClient;
import net.fabricmc.api.ClientModInitializer;

public class BcsFurnishingsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		DollClientHooks.register();
		HeadGeneratorClient.register();
	}
}
