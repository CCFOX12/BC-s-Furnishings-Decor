package cc.fox.bcsfurnishings.jade;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class BcsFurnishingsJadePlugin implements IWailaPlugin {
	@Override
	public void registerClient(IWailaClientRegistration registration) {
		if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) {
			return;
		}
		try {
			Class.forName("cc.fox.bcsfurnishings.client.jade.DollJadeClient")
					.getMethod("register", IWailaClientRegistration.class)
					.invoke(null, registration);
		} catch (ReflectiveOperationException exception) {
			throw new IllegalStateException("Jade client hook missing", exception);
		}
	}
}
