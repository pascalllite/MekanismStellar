package mekanism.stellar.common.registries;

import mekanism.api.Upgrade;
import mekanism.common.block.attribute.AttributeParticleFX;
import mekanism.common.lib.math.Pos3D;
import mekanism.generators.common.content.blocktype.BlockShapes;
import mekanism.generators.common.content.blocktype.Generator;
import mekanism.generators.common.registries.GeneratorsSounds;
import mekanism.stellar.common.StellarLang;
import mekanism.stellar.common.config.StellarConfig;
import mekanism.stellar.common.tile.TileEntityStellarGenerator;
import net.minecraft.core.particles.ParticleTypes;

import java.util.EnumSet;

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

    private StellarBlockTypes() {
    }
}
