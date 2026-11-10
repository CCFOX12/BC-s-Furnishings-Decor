package cc.fox.bcsfurnishings.client.fashion;

import cc.fox.bcsfurnishings.BcsFurnishings;
import cc.fox.bcsfurnishings.fashion.FashionCatalog;
import cc.fox.bcsfurnishings.fashion.FashionEntry;
import cc.fox.bcsfurnishings.fashion.FashionKind;
import com.mojang.blaze3d.platform.NativeImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureContents;
import net.minecraft.core.ClientAsset;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;

public final class FashionSkinComposer {
	private static final int SKIN = 64;
	private static final int OPAQUE = 8;
	private static final int[][] PAIRS = {
			{0, 0, 32, 0, 32, 16},
			{0, 16, 0, 32, 16, 16},
			{16, 16, 16, 32, 24, 16},
			{40, 16, 40, 32, 16, 16},
			{16, 48, 0, 48, 16, 16},
			{32, 48, 48, 48, 16, 16}
	};
	private static final Map<String, NativeImage> FASHION_IMAGES = new HashMap<>();

	private FashionSkinComposer() {
	}

	public static NativeImage bake(ClientAsset.Texture body, ItemStack[] pieces) {
		NativeImage dest = openBody(body);
		if (dest == null) {
			return null;
		}
		for (ItemStack piece : pieces) {
			FashionEntry entry = FashionCatalog.of(piece);
			if (entry == null || entry.kind() != FashionKind.TEXTURE) {
				continue;
			}
			NativeImage fashion = fashionImage(entry);
			if (fashion != null) {
				overlay(dest, fashion);
			}
		}
		return dest;
	}

	public static void clearFashionImages() {
		for (NativeImage image : FASHION_IMAGES.values()) {
			image.close();
		}
		FASHION_IMAGES.clear();
	}

	private static NativeImage fashionImage(FashionEntry entry) {
		NativeImage cached = FASHION_IMAGES.get(entry.id());
		if (cached != null) {
			return cached;
		}
		NativeImage loaded = loadResource(entry.texturePath());
		if (loaded == null) {
			BcsFurnishings.LOGGER.warn("Missing fashion texture {}", entry.texturePath());
			return null;
		}
		NativeImage normalized = normalizeOwned(loaded);
		FASHION_IMAGES.put(entry.id(), normalized);
		return normalized;
	}

	private static NativeImage openBody(ClientAsset.Texture body) {
		NativeImage fromPack = loadResource(body.texturePath());
		if (fromPack != null) {
			return normalizeOwned(fromPack);
		}
		if (Minecraft.getInstance().getTextureManager().getTexture(body.texturePath())
				instanceof net.minecraft.client.renderer.texture.DynamicTexture dynamic
				&& dynamic.getPixels() != null) {
			return normalizeOwned(copyImage(dynamic.getPixels()));
		}
		BcsFurnishings.LOGGER.warn("Could not read player skin {}", body.texturePath());
		return null;
	}

	private static NativeImage loadResource(net.minecraft.resources.Identifier id) {
		try {
			TextureContents contents = TextureContents.load(Minecraft.getInstance().getResourceManager(), id);
			try {
				return copyImage(contents.image());
			} finally {
				contents.close();
			}
		} catch (IOException ignored) {
			return null;
		}
	}

	private static NativeImage copyImage(NativeImage src) {
		NativeImage dest = new NativeImage(src.getWidth(), src.getHeight(), true);
		dest.copyFrom(src);
		return dest;
	}

	private static NativeImage normalizeOwned(NativeImage owned) {
		int width = owned.getWidth();
		int height = owned.getHeight();
		if (width == SKIN && height == SKIN) {
			return owned;
		}
		if (width == SKIN && height == 32) {
			NativeImage converted = convertLegacy(owned);
			owned.close();
			return converted;
		}
		NativeImage scaled = new NativeImage(SKIN, SKIN, true);
		owned.resizeSubRectTo(0, 0, width, height, scaled);
		owned.close();
		return scaled;
	}

	private static NativeImage convertLegacy(NativeImage src) {
		NativeImage dest = new NativeImage(SKIN, SKIN, true);
		dest.copyFrom(src);
		dest.fillRect(0, 32, SKIN, 32, 0);
		dest.copyRect(4, 16, 16, 32, 4, 4, true, false);
		dest.copyRect(8, 16, 16, 32, 4, 4, true, false);
		dest.copyRect(0, 20, 24, 32, 4, 12, true, false);
		dest.copyRect(4, 20, 16, 32, 4, 12, true, false);
		dest.copyRect(8, 20, 8, 32, 4, 12, true, false);
		dest.copyRect(12, 20, 16, 32, 4, 12, true, false);
		dest.copyRect(44, 16, -8, 32, 4, 4, true, false);
		dest.copyRect(48, 16, -8, 32, 4, 4, true, false);
		dest.copyRect(40, 20, 0, 32, 4, 12, true, false);
		dest.copyRect(44, 20, -8, 32, 4, 12, true, false);
		dest.copyRect(48, 20, -16, 32, 4, 12, true, false);
		dest.copyRect(52, 20, -8, 32, 4, 12, true, false);
		return dest;
	}

	private static void overlay(NativeImage dest, NativeImage fashion) {
		for (int y = 0; y < SKIN; y++) {
			for (int x = 0; x < SKIN; x++) {
				int pixel = fashion.getPixel(x, y);
				if (ARGB.alpha(pixel) >= OPAQUE) {
					dest.setPixel(x, y, pixel);
				}
			}
		}
		for (int[] pair : PAIRS) {
			int innerX = pair[0];
			int innerY = pair[1];
			int outerX = pair[2];
			int outerY = pair[3];
			int width = pair[4];
			int height = pair[5];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int inner = fashion.getPixel(innerX + x, innerY + y);
					int outer = fashion.getPixel(outerX + x, outerY + y);
					if (ARGB.alpha(inner) >= OPAQUE && ARGB.alpha(outer) < OPAQUE) {
						dest.setPixel(outerX + x, outerY + y, 0);
					}
				}
			}
		}
	}
}
