package cc.fox.bcsfurnishings.client.headgenerator;

import cc.fox.bcsfurnishings.headgenerator.HeadGeneratorOpenPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public final class HeadGeneratorClient {
	private HeadGeneratorClient() {
	}

	public static void register() {
		ClientPlayNetworking.registerGlobalReceiver(HeadGeneratorOpenPayload.TYPE, (payload, context) ->
				context.client().setScreen(new HeadGeneratorScreen(payload.pos(), payload.name())));
	}
}
