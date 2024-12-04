package mekanism.stellar.common.config;

import mekanism.common.config.MekanismConfigHelper;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;

public class StellarConfig {
    public static final StellarMachineConfig machine = new StellarMachineConfig();
    public static final StellarEternalHeatGeneratorConfig eternalHeatGenerator = new StellarEternalHeatGeneratorConfig();

    private StellarConfig() {
    }

    public static void registerConfigs(ModLoadingContext modLoadingContext) {
        ModContainer modContainer = modLoadingContext.getActiveContainer();
        MekanismConfigHelper.registerConfig(modContainer, machine);
        MekanismConfigHelper.registerConfig(modContainer, eternalHeatGenerator);
    }
}
