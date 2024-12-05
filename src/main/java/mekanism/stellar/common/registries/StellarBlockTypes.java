package mekanism.stellar.common.registries;

import mekanism.api.Upgrade;
import mekanism.common.block.attribute.AttributeParticleFX;
import mekanism.common.lib.math.Pos3D;
import mekanism.generators.common.content.blocktype.BlockShapes;
import mekanism.generators.common.content.blocktype.Generator;
import mekanism.generators.common.registries.GeneratorsSounds;
import mekanism.stellar.common.CompressedEternalHeatGenerators;
import mekanism.stellar.common.StellarLang;
import mekanism.stellar.common.config.StellarConfig;
import mekanism.stellar.common.tile.TileEntityCompressedEternalHeatGenerator;
import mekanism.stellar.common.tile.TileEntityEternalHeatGenerator;
import mekanism.stellar.common.tile.TileEntityStellarGenerator;
import net.minecraft.core.particles.ParticleTypes;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StellarBlockTypes {
    public static final Generator<TileEntityStellarGenerator> STELLAR_GENERATOR = Generator.GeneratorBuilder
            .createGenerator(() -> StellarTileEntityTypes.STELLAR_GENERATOR, StellarLang.DESCRIPTION_STELLAR_GENERATOR)
            .withGui(() -> StellarContainerTypes.STELLAR_GENERATOR)
            .withEnergyConfig(StellarConfig.machine.energyCapacity)
            .withCustomShape((BlockShapes.HEAT_GENERATOR))
            .withSound(GeneratorsSounds.HEAT_GENERATOR)
            .withSupportedUpgrades(EnumSet.of(Upgrade.MUFFLING))
            .with(new AttributeParticleFX()
                    .add(ParticleTypes.SMOKE, rand -> new Pos3D(rand.nextFloat() * 0.6F - 0.3F, rand.nextFloat() * 6.0F / 16.0F, -0.52))
                    .add(ParticleTypes.FLAME, rand -> new Pos3D(rand.nextFloat() * 0.6F - 0.3F, rand.nextFloat() * 6.0F / 16.0F, -0.52)))
            .build();

    public static final Generator<TileEntityEternalHeatGenerator> ETERNAL_HEAT_GENERATOR = Generator.GeneratorBuilder
            .createGenerator(() -> StellarTileEntityTypes.ETERNAL_HEAT_GENERATOR, StellarLang.DESCRIPTION_ETERNAL_HEAT_GENERATOR)
            .withGui(() -> StellarContainerTypes.ETERNAL_HEAT_GENERATOR)
            .withEnergyConfig(StellarConfig.eternalHeatGenerator.capacity)
            .withCustomShape((BlockShapes.HEAT_GENERATOR))
            .withSound(GeneratorsSounds.HEAT_GENERATOR)
            .withSupportedUpgrades(EnumSet.of(Upgrade.MUFFLING))
            .with(new AttributeParticleFX()
                    .add(ParticleTypes.SMOKE, rand -> new Pos3D(rand.nextFloat() * 0.6F - 0.3F, rand.nextFloat() * 6.0F / 16.0F, -0.52))
                    .add(ParticleTypes.FLAME, rand -> new Pos3D(rand.nextFloat() * 0.6F - 0.3F, rand.nextFloat() * 6.0F / 16.0F, -0.52)))
            .build();

    public static final EnumMap<CompressedEternalHeatGenerators, Generator<TileEntityCompressedEternalHeatGenerator>> COMPRESSED_ETERNAL_HEAT_GENERATORS = Stream.of(CompressedEternalHeatGenerators.values()).collect(Collectors.toMap(UnaryOperator.identity(), type -> Generator.GeneratorBuilder
            .createGenerator(() -> StellarTileEntityTypes.COMPRESSED_ETERNAL_HEAT_GENERATORS.get(type), type)
            .withGui(() -> StellarContainerTypes.COMPRESSED_ETERNAL_HEAT_GENERATORS.get(type))
            .withEnergyConfig(StellarConfig.eternalHeatGenerator.compressedCapacities.get(type))
            .withCustomShape((BlockShapes.HEAT_GENERATOR))
            .withSound(GeneratorsSounds.HEAT_GENERATOR)
            .withSupportedUpgrades(EnumSet.of(Upgrade.MUFFLING))
            .with(new AttributeParticleFX()
                    .add(ParticleTypes.SMOKE, rand -> new Pos3D(rand.nextFloat() * 0.6F - 0.3F, rand.nextFloat() * 6.0F / 16.0F, -0.52))
                    .add(ParticleTypes.FLAME, rand -> new Pos3D(rand.nextFloat() * 0.6F - 0.3F, rand.nextFloat() * 6.0F / 16.0F, -0.52)))
            .build(), (a, b) -> a, () -> new EnumMap<>(CompressedEternalHeatGenerators.class)));

    private StellarBlockTypes() {
    }
}
