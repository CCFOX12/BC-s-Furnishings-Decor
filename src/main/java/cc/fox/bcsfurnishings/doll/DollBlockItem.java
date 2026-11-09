package cc.fox.bcsfurnishings.doll;

import dev.emi.trinkets.api.TrinketInventory;
import dev.emi.trinkets.api.TrinketsApi;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class DollBlockItem extends BlockItem implements GeoItem {
	private static final String CLIENT_HOOKS = "cc.fox.bcsfurnishings.client.doll.DollItemRenderers";
	private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

	public DollBlockItem(DollBlock block, Item.Properties properties) {
		super(block, properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack held = player.getItemInHand(hand);
		TrinketInventory cosmetic = cosmeticHead(player);
		if (cosmetic == null || held.isEmpty()) {
			return InteractionResult.FAIL;
		}
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}

		ItemStack equipped = cosmetic.getItem(0).copy();
		ItemStack toWear = player.hasInfiniteMaterials() ? held.copyWithCount(1) : held.split(1);
		cosmetic.setItem(0, toWear);
		cosmetic.markUpdate();
		if (!equipped.isEmpty()) {
			if (held.isEmpty()) {
				player.setItemInHand(hand, equipped);
			} else {
				player.getInventory().placeItemBackInInventory(equipped);
			}
		}
		level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WOOL_PLACE, SoundSource.PLAYERS, 1.0F, 1.0F);
		return InteractionResult.SUCCESS;
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
		tooltip.accept(Component.translatable("tooltip.bcs_furnishings.cosmetic_head").withStyle(ChatFormatting.GRAY));
	}

	@Override
	public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
		try {
			Class<?> hooks = Class.forName(CLIENT_HOOKS);
			Method accept = hooks.getMethod("accept", DollBlockItem.class, Consumer.class);
			accept.invoke(null, this, consumer);
		} catch (ReflectiveOperationException exception) {
			throw new IllegalStateException("Doll item renderer is client-only", exception);
		}
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(test -> PlayState.STOP));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.geoCache;
	}

	private static TrinketInventory cosmeticHead(Player player) {
		return TrinketsApi.getTrinketComponent(player)
				.map(component -> {
					Map<String, TrinketInventory> head = component.getInventory().get("head");
					if (head == null) {
						return null;
					}
					TrinketInventory cosmetic = head.get("cosmetic");
					if (cosmetic == null || cosmetic.getContainerSize() < 1) {
						return null;
					}
					return cosmetic;
				})
				.orElse(null);
	}
}
