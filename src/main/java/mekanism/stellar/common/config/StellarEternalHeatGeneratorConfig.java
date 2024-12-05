package mekanism.stellar.common.config;

import mekanism.api.math.FloatingLong;
import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.value.CachedFloatingLongValue;
import mekanism.stellar.common.CompressedEternalHeatGenerators;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import java.util.EnumMap;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StellarEternalHeatGeneratorConfig extends BaseMekanismConfig {
    public final CachedFloatingLongValue capacity;
    public final CachedFloatingLongValue generation;

    public final EnumMap<CompressedEternalHeatGenerators, CachedFloatingLongValue> compressedCapacities;
    public final EnumMap<CompressedEternalHeatGenerators, CachedFloatingLongValue> compressedGeneration;

    private final ForgeConfigSpec configSpec;

    public StellarEternalHeatGeneratorConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Eternal Heat Generator Config. This config is synced between server and client.").push("eternalHeatGenerator");
        capacity = CachedFloatingLongValue.define(this, builder, "Maximum energy buffer of the Eternal Heat Generator", "capacity", FloatingLong.createConst(131_072));
        generation = CachedFloatingLongValue.define(this, builder, "Amount of energy produced by the Eternal Heat Generator per tick", "generation", FloatingLong.createConst(128));

        builder.comment("Compressed Eternal Heat Generator Config").push("compressed");
        compressedCapacities = Stream.of(CompressedEternalHeatGenerators.values()).collect(Collectors.toMap(UnaryOperator.identity(), type -> CachedFloatingLongValue.define(this, builder, "Maximum energy buffer of the " + type.multiplier() + "x Compressed Eternal Heat Generator", "compressedEternalHeatGenerator" + type.multiplier() + "xCapacity", FloatingLong.createConst(131_072L * type.multiplier())), (a, b) -> a, () -> new EnumMap<>(CompressedEternalHeatGenerators.class)));
        compressedGeneration = Stream.of(CompressedEternalHeatGenerators.values()).collect(Collectors.toMap(UnaryOperator.identity(), type -> CachedFloatingLongValue.define(this, builder, "Amount of energy produced by the " + type.multiplier() + "x Compressed Eternal Heat Generator per tick", "compressedEternalHeatGenerator" + type.multiplier() + "xGeneration", FloatingLong.createConst(128L * type.multiplier())), (a, b) -> a, () -> new EnumMap<>(CompressedEternalHeatGenerators.class)));
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
