package mekanism.stellar.common.registries;

import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.stellar.common.CompressedEternalHeatGenerators;
import mekanism.stellar.common.Stellar;
import mekanism.stellar.common.tile.TileEntityCompressedEternalHeatGenerator;
import mekanism.stellar.common.tile.TileEntityEternalHeatGenerator;
import mekanism.stellar.common.tile.TileEntityStellarGenerator;

import java.util.EnumMap;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StellarTileEntityTypes {
    public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(Stellar.MODID);

    public static final TileEntityTypeRegistryObject<TileEntityStellarGenerator> STELLAR_GENERATOR = TILE_ENTITY_TYPES.register(StellarBlocks.STELLAR_GENERATOR, TileEntityStellarGenerator::new);

    public static final TileEntityTypeRegistryObject<TileEntityEternalHeatGenerator> ETERNAL_HEAT_GENERATOR = TILE_ENTITY_TYPES.register(StellarBlocks.ETERNAL_HEAT_GENERATOR, TileEntityEternalHeatGenerator::new);

    public static final EnumMap<CompressedEternalHeatGenerators, TileEntityTypeRegistryObject<TileEntityCompressedEternalHeatGenerator>> COMPRESSED_ETERNAL_HEAT_GENERATORS = Stream.of(CompressedEternalHeatGenerators.values()).collect(Collectors.toMap(UnaryOperator.identity(), type -> TILE_ENTITY_TYPES.register(StellarBlocks.COMPRESSED_ETERNAL_HEAT_GENERATORS.get(type), (pos, state) -> new TileEntityCompressedEternalHeatGenerator(pos, state, type)), (a, b) -> a, () -> new EnumMap<>(CompressedEternalHeatGenerators.class)));

    private StellarTileEntityTypes() {
    }
}
