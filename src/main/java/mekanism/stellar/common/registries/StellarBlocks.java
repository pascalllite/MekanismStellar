package mekanism.stellar.common.registries;

import mekanism.common.block.prefab.BlockTile.BlockTileModel;
import mekanism.common.item.block.machine.ItemBlockMachine;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.generators.common.content.blocktype.Generator;
import mekanism.stellar.common.CompressedEternalHeatGenerators;
import mekanism.stellar.common.Stellar;
import mekanism.stellar.common.tile.TileEntityCompressedEternalHeatGenerator;
import mekanism.stellar.common.tile.TileEntityEternalHeatGenerator;
import mekanism.stellar.common.tile.TileEntityStellarGenerator;

import java.util.EnumMap;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StellarBlocks {
    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(Stellar.MODID);

    public static final BlockRegistryObject<BlockTileModel<TileEntityStellarGenerator, Generator<TileEntityStellarGenerator>>, ItemBlockMachine> STELLAR_GENERATOR = BLOCKS.register("stellar_generator", () -> new BlockTileModel<>(StellarBlockTypes.STELLAR_GENERATOR), ItemBlockMachine::new);

    public static final BlockRegistryObject<BlockTileModel<TileEntityEternalHeatGenerator, Generator<TileEntityEternalHeatGenerator>>, ItemBlockMachine> ETERNAL_HEAT_GENERATOR = BLOCKS.register("eternal_heat_generator", () -> new BlockTileModel<>(StellarBlockTypes.ETERNAL_HEAT_GENERATOR), ItemBlockMachine::new);

    public static final EnumMap<CompressedEternalHeatGenerators, BlockRegistryObject<BlockTileModel<TileEntityCompressedEternalHeatGenerator, Generator<TileEntityCompressedEternalHeatGenerator>>, ItemBlockMachine>> COMPRESSED_ETERNAL_HEAT_GENERATORS = Stream.of(CompressedEternalHeatGenerators.values()).collect(Collectors.toMap(UnaryOperator.identity(), type -> BLOCKS.register(type.path(), () -> new BlockTileModel<>(StellarBlockTypes.COMPRESSED_ETERNAL_HEAT_GENERATORS.get(type)), ItemBlockMachine::new), (a, b) -> a, () -> new EnumMap<>(CompressedEternalHeatGenerators.class)));

    private StellarBlocks() {
    }
}
