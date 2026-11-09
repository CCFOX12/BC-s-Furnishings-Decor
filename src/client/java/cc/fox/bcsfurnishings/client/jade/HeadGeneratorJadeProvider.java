package cc.fox.bcsfurnishings.client.jade;

import cc.fox.bcsfurnishings.BcsFurnishings;
import cc.fox.bcsfurnishings.headgenerator.HeadGeneratorBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum HeadGeneratorJadeProvider implements IBlockComponentProvider {
	INSTANCE;

	@Override
	public Identifier getUid() {
		return BcsFurnishings.id("head_generator");
	}

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!(accessor.getBlockEntity() instanceof HeadGeneratorBlockEntity blockEntity)) {
			return;
		}
		String name = blockEntity.getPlayerName();
		if (!name.isEmpty()) {
			tooltip.add(Component.literal(name));
		}
	}
}
