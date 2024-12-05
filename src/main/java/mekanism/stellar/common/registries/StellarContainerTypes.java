package mekanism.stellar.common.registries;

import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeDeferredRegister;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import mekanism.stellar.common.CompressedEternalHeatGenerators;
import mekanism.stellar.common.Stellar;
import mekanism.stellar.common.tile.TileEntityCompressedEternalHeatGenerator;
import mekanism.stellar.common.tile.TileEntityEternalHeatGenerator;
import mekanism.stellar.common.tile.TileEntityStellarGenerator;

import java.util.EnumMap;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StellarContainerTypes {
    public static final ContainerTypeDeferredRegister CONTAINER_TYPES = new ContainerTypeDeferredRegister(Stellar.MODID);

    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityStellarGenerator>> STELLAR_GENERATOR = CONTAINER_TYPES.register(StellarBlocks.STELLAR_GENERATOR, TileEntityStellarGenerator.class);

    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityEternalHeatGenerator>> ETERNAL_HEAT_GENERATOR = CONTAINER_TYPES.register(StellarBlocks.ETERNAL_HEAT_GENERATOR, TileEntityEternalHeatGenerator.class);

    public static final EnumMap<CompressedEternalHeatGenerators, ContainerTypeRegistryObject<MekanismTileContainer<TileEntityCompressedEternalHeatGenerator>>> COMPRESSED_ETERNAL_HEAT_GENERATORS = Stream.of(CompressedEternalHeatGenerators.values()).collect(Collectors.toMap(UnaryOperator.identity(), type -> CONTAINER_TYPES.register(StellarBlocks.COMPRESSED_ETERNAL_HEAT_GENERATORS.get(type), TileEntityCompressedEternalHeatGenerator.class), (e1, e2) -> e1, () -> new EnumMap<>(CompressedEternalHeatGenerators.class)));

    private StellarContainerTypes() {
    }
}
