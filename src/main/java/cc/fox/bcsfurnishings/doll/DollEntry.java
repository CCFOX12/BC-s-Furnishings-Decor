package cc.fox.bcsfurnishings.doll;

import cc.fox.bcsfurnishings.BcsFurnishings;
import net.minecraft.resources.Identifier;

public record DollEntry(String folder, String stem, String id) {
	public Identifier identifier() {
		return BcsFurnishings.id(this.id);
	}
}
