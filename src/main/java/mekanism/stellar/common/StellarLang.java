package mekanism.stellar.common;

import mekanism.api.text.ILangEntry;
import net.minecraft.Util;

public enum StellarLang implements ILangEntry {
    HEAT_LOSS("gui", "heat.loss"),
    COOLING_TARGET("gui", "cooling.target"),
    DESCRIPTION_STELLAR_GENERATOR("description", "stellar_generator"),

    DESCRIPTION_ETERNAL_HEAT_GENERATOR("description", "eternal_heat_generator");

    private final String key;

    StellarLang(String type, String path) {
        this(Util.makeDescriptionId(type, Stellar.rl(path)));
    }

    StellarLang(String key) {
        this.key = key;
    }

    @Override
    public String getTranslationKey() {
        return key;
    }
}
