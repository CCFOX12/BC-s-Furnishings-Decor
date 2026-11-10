package cc.fox.bcsfurnishings.fashion;

import cc.fox.bcsfurnishings.BcsFurnishings;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;

public record FashionEntry(String id, FashionKind kind, EquipmentSlot slot, boolean fullSet) {
	public Identifier identifier() {
		return BcsFurnishings.id(this.id);
	}

	public Identifier texturePath() {
		return BcsFurnishings.id("textures/fashion/" + this.id + ".png");
	}

	public String trinketGroup() {
		return this.slot.getName();
	}
}
