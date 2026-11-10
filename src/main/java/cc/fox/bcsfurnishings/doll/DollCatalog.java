package cc.fox.bcsfurnishings.doll;

import java.util.List;

/**
 * Folder order matches {@code 模型工程文件/模型资产}. Only pairs copied into
 * {@code assets/bcs_furnishings} are listed.
 */
public final class DollCatalog {
	public static final List<DollEntry> ENTRIES = List.of(
			entry("AaBo_duck", "AaBo_duck_ha"),
			entry("AaBo_duck", "AaBo_duck_sit"),
			entry("AaBo_duck", "AaBo_duck_sit_2"),
			entry("AaBo_duck", "AaBo_duck_sit_nl"),
			entry("AaBo_duck", "AaBo_duck_sp"),
			entry("ADL_Huoni_Queen", "ADL_Huoni_Queen_sit"),
			entry("ALittle_bird", "ALittle_bird_sit"),
			entry("banana23333", "banana23333_sit"),
			entry("BCS23", "BCS23_sit"),
			entry("Bit_Rush_7", "Bit_Rush_7_sit"),
			entry("CaffNic", "CaffNic_sit"),
			entry("Chelsie_seven7", "Chelsie_seven7_sit"),
			entry("CHENLIN06", "CHENLIN06_chenmoamoa_maid_1"),
			entry("CHENLIN06", "CHENLIN06_maid_sit"),
			entry("CHENLIN06", "CHENLIN06_sit"),
			entry("chenmoamoa", "chenmoamoa_cowboy_sit"),
			entry("chenmoamoa", "chenmoamoa_cs_xd"),
			entry("chenmoamoa", "chenmoamoa_maid_sit"),
			entry("chenmoamoa", "chenmoamoa_nl_sit"),
			entry("chenmoamoa", "chenmoamoa_sit"),
			entry("Elevenyears11", "Elevenyears11_sit"),
			entry("jing_lan10086", "jing_lan10086_sit"),
			entry("jingzhubamjiu", "jingzhubamjiu_sit"),
			entry("kagurachann", "kagurachann_sit"),
			entry("Kuluservski", "Kuluservski_sit"),
			entry("Mewlamb", "Mewlamb_sit"),
			entry("MuMU___123", "MuMU___123_sit"),
			entry("PPP", "PPP_sit"),
			entry("sjs15612075998", "sjs15612075998_sit"),
			entry("Sleeping_Cloud_1", "Sleeping_Cloud_1_nl_sit"),
			entry("TopiseaNotFound", "TopiseaNotFound_sit"),
			entry("TopiseaNotFound", "TopiseaNotFound_sp"),
			entry("wSogee_M", "wSogee_M_Duck_sp"),
			entry("wSogee_M", "wSogee_M_sit"),
			entry("XLT__Coin", "XLT__Coin_dinosaur_sit"),
			entry("XLT__Coin", "XLT__Coin_dinosaur_sp"),
			entry("XLT__Coin", "XLT__Coin_sit"),
			entry("XLT__Coin", "XLT__Coin_sp"),
			entry("YouHane23", "YouHane23_sit"),
			entry("Zebb520", "Zebb520_sit"),
			entry("大拼图", "group_photo_xd")
	);

	private DollCatalog() {
	}

	private static DollEntry entry(String folder, String stem) {
		return new DollEntry(folder, stem, stem.toLowerCase());
	}
}
