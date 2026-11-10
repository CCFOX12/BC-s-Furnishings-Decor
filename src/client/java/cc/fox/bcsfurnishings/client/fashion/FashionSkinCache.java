package cc.fox.bcsfurnishings.client.fashion;

import cc.fox.bcsfurnishings.BcsFurnishings;
import cc.fox.bcsfurnishings.fashion.FashionCatalog;
import cc.fox.bcsfurnishings.fashion.FashionEntry;
import cc.fox.bcsfurnishings.fashion.FashionKind;
import com.mojang.blaze3d.platform.NativeImage;
import io.github.apace100.cosmetic_armor.CosmeticArmor;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.item.ItemStack;

public final class FashionSkinCache {
	private static final int MAX_ENTRIES = 48;
	private static final EquipmentSlot[] SLOT_ORDER = {
			EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD
	};
	private static long nextTextureId;
	private static final Map<String, CacheEntry> CACHE = new LinkedHashMap<>(64, 0.75F, true) {
		@Override
		protected boolean removeEldestEntry(Map.Entry<String, CacheEntry> eldest) {
			if (size() <= MAX_ENTRIES) {
				return false;
			}
			eldest.getValue().close();
			return true;
		}
	};

	private FashionSkinCache() {
	}

	public static void apply(Avatar entity, AvatarRenderState state) {
		ItemStack[] pieces = equippedTexturePieces(entity);
		if (pieces.length == 0) {
			return;
		}
		String key = cacheKey(state.skin, pieces);
		CacheEntry cached = CACHE.get(key);
		if (cached == null) {
			NativeImage baked = FashionSkinComposer.bake(state.skin.body(), pieces);
			if (baked == null) {
				return;
			}
			ClientAsset.ResourceTexture asset = new ClientAsset.ResourceTexture(
					BcsFurnishings.id("fashion_baked/" + Long.toUnsignedString(nextTextureId++)));
			Identifier gpuId = asset.texturePath();
			DynamicTexture texture = new DynamicTexture(() -> "bcs fashion " + gpuId.getPath(), baked);
			Minecraft.getInstance().getTextureManager().register(gpuId, texture);
			PlayerSkin patched = state.skin.with(PlayerSkin.Patch.create(
					Optional.of(asset),
					Optional.empty(),
					Optional.empty(),
					Optional.empty()
			));
			cached = new CacheEntry(gpuId, patched, texture);
			CACHE.put(key, cached);
		}
		state.skin = cached.skin();
	}

	public static void clear() {
		for (CacheEntry entry : CACHE.values()) {
			entry.close();
		}
		CACHE.clear();
		FashionSkinComposer.clearFashionImages();
	}

	private static ItemStack[] equippedTexturePieces(Avatar entity) {
		ItemStack chest = CosmeticArmor.getCosmeticArmor(entity, EquipmentSlot.CHEST);
		if (FashionCatalog.isFullSet(chest) && isTexture(chest)) {
			return new ItemStack[]{chest};
		}
		ItemStack[] ordered = new ItemStack[SLOT_ORDER.length];
		int count = 0;
		for (int i = 0; i < SLOT_ORDER.length; i++) {
			ItemStack stack = CosmeticArmor.getCosmeticArmor(entity, SLOT_ORDER[i]);
			if (isTexture(stack)) {
				ordered[i] = stack;
				count++;
			} else {
				ordered[i] = ItemStack.EMPTY;
			}
		}
		if (count == 0) {
			return new ItemStack[0];
		}
		return ordered;
	}

	private static boolean isTexture(ItemStack stack) {
		FashionEntry entry = FashionCatalog.of(stack);
		return entry != null && entry.kind() == FashionKind.TEXTURE;
	}

	private static String cacheKey(PlayerSkin skin, ItemStack[] pieces) {
		StringBuilder builder = new StringBuilder(skin.body().id().toString());
		for (ItemStack piece : pieces) {
			builder.append('|');
			if (piece.isEmpty()) {
				builder.append("empty");
			} else {
				builder.append(BuiltInRegistries.ITEM.getKey(piece.getItem()));
			}
		}
		return builder.toString();
	}

	private record CacheEntry(Identifier id, PlayerSkin skin, DynamicTexture texture) {
		private void close() {
			Minecraft.getInstance().getTextureManager().release(this.id);
		}
	}
}
