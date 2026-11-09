package cc.fox.bcsfurnishings.client.jade;

import cc.fox.bcsfurnishings.BcsFurnishings;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum DollJadeProvider implements IBlockComponentProvider {
	INSTANCE;

	@Override
	public Identifier getUid() {
		return BcsFurnishings.id("cosmetic_head");
	}

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		tooltip.add(Component.translatable("jade.bcs_furnishings.cosmetic_head"));
	}
}
