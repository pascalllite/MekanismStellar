package mekanism.stellar.client;

import mekanism.stellar.common.Stellar;
import mekanism.stellar.common.StellarLang;
import mekanism.stellar.common.registries.StellarBlocks;
import net.minecraft.data.DataGenerator;

public class StellarLangProvider extends BaseLanguageProvider {

    public StellarLangProvider(DataGenerator gen) {
        super(gen, Stellar.MODID);
    }

    @Override
    protected void addTranslations() {
        addBlocks();
        addMisc();
    }

    public void addBlocks() {
        add(StellarBlocks.STELLAR_GENERATOR, "Stellar Generator");
        add(StellarBlocks.ETERNAL_HEAT_GENERATOR, "Eternal Heat Generator");
    }

    public void addMisc() {
        add(StellarLang.HEAT_LOSS, "Heat Loss: %s/t");
        add(StellarLang.COOLING_TARGET, "Cooling Target: %s");

        add(StellarLang.DESCRIPTION_STELLAR_GENERATOR, "Get even hotter and hotter and hotter and HOTTTTTTTTTTTTTTTTTTTTTTTTTTTTTER!!!!!!!!");
        add(StellarLang.DESCRIPTION_ETERNAL_HEAT_GENERATOR, "Over using the power of Mekanism...");
    }
}
