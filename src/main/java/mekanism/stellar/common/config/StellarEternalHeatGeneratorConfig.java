package mekanism.stellar.common.config;

import mekanism.api.math.FloatingLong;
import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.value.CachedFloatingLongValue;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

public class StellarEternalHeatGeneratorConfig extends BaseMekanismConfig {
    public final CachedFloatingLongValue capacity;
    public final CachedFloatingLongValue generation;

    private final ForgeConfigSpec configSpec;

    public StellarEternalHeatGeneratorConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Eternal Heat Generator Config. This config is synced between server and client.").push("eternalHeatGenerator");
        capacity = CachedFloatingLongValue.define(this, builder, "Maximum energy buffer of the Eternal Heat Generator", "capacity", FloatingLong.createConst(131_072));
        generation = CachedFloatingLongValue.define(this, builder, "Amount of energy produced by the Eternal Heat Generator per tick", "generation", FloatingLong.createConst(128));
        builder.pop();
        configSpec = builder.build();
    }

    @Override
    public String getFileName() {
        return "eternalHeatGenerator";
    }

    @Override
    public ForgeConfigSpec getConfigSpec() {
        return configSpec;
    }

    @Override
    public ModConfig.Type getConfigType() {
        return ModConfig.Type.SERVER;
    }
}
