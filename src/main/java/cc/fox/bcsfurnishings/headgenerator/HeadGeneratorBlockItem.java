package cc.fox.bcsfurnishings.headgenerator;

import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class HeadGeneratorBlockItem extends BlockItem {
	public HeadGeneratorBlockItem(HeadGeneratorBlock block, Item.Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(
			ItemStack stack,
			TooltipContext context,
			TooltipDisplay display,
			Consumer<Component> tooltip,
			TooltipFlag flag
	) {
		super.appendHoverText(stack, context, display, tooltip, flag);
		tooltip.accept(Component.translatable("tooltip.bcs_furnishings.head_generator.op").withStyle(ChatFormatting.GRAY));
		tooltip.accept(Component.translatable("tooltip.bcs_furnishings.head_generator.redstone").withStyle(ChatFormatting.GRAY));
	}
}
