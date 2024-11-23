package mekanism.stellar.common.config;

import mekanism.api.math.FloatingLong;
import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.value.CachedDoubleValue;
import mekanism.common.config.value.CachedFloatingLongValue;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;

public class StellarMachineConfig extends BaseMekanismConfig {
    public final CachedDoubleValue energyDurationMultiplier;
    public final CachedFloatingLongValue energyCapacity;

    private final ForgeConfigSpec configSpec;

    StellarMachineConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Stellar Generator Config. This config is synced between server and client.").push("stellarGenerator");
        energyDurationMultiplier = CachedDoubleValue.wrap(this, builder.comment("Multiplier for energy generation duration").define("energyDurationMultiplier", 1.0));
        energyCapacity = CachedFloatingLongValue.define(this, builder, "Energy capacity of the Stellar Generator", "energyCapacity", FloatingLong.MAX_VALUE);
        builder.pop();
        configSpec = builder.build();
    }

    @Override
    public String getFileName() {
        return "generator";
    }

    @Override
    public ForgeConfigSpec getConfigSpec() {
        return configSpec;
    }

    @Override
    public Type getConfigType() {
        return Type.SERVER;
    }
}
