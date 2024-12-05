package mekanism.stellar.common;

import mekanism.api.text.ILangEntry;
import net.minecraft.Util;

public enum StellarLang implements ILangEntry {
    HEAT_LOSS("gui", "heat.loss"),
    COOLING_TARGET("gui", "cooling.target"),
    DESCRIPTION_STELLAR_GENERATOR("description", "stellar_generator"),

    DESCRIPTION_ETERNAL_HEAT_GENERATOR("description", "eternal_heat_generator"),

    DESCRIPTION_COMPRESSED_ETERNAL_HEAT_GENERATOR("description", "compressed_ehg_base");

    private final String key;
    private final String type;
    private CompressedEternalHeatGenerators compressed_ehg_type = null;

    StellarLang(String type, String path) {
        this.type = type;
        this.key = Util.makeDescriptionId(type, Stellar.rl(path));
    }

    public StellarLang type(CompressedEternalHeatGenerators type) {
        if (this != DESCRIPTION_COMPRESSED_ETERNAL_HEAT_GENERATOR) {
            throw new IllegalStateException("Can only set type for \"Compressed Eternal Heat Generator\" description");
        }
        this.compressed_ehg_type = type;
        return this;
    }

    @Override
    public String getTranslationKey() {
        if (this == DESCRIPTION_COMPRESSED_ETERNAL_HEAT_GENERATOR) {
            if (this.compressed_ehg_type == null) {
                throw new IllegalStateException("No \"Compressed Eternal Heat Generator\" type set for description");
            }
            return Util.makeDescriptionId(type, Stellar.rl(compressed_ehg_type.path()));
        }
        return key;
    }
}
