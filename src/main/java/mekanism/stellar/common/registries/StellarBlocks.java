package mekanism.stellar.common.registries;

import mekanism.common.block.prefab.BlockTile.BlockTileModel;
import mekanism.common.item.block.machine.ItemBlockMachine;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.generators.common.content.blocktype.Generator;
import mekanism.stellar.common.Stellar;
import mekanism.stellar.common.tile.TileEntityEternalHeatGenerator;
import mekanism.stellar.common.tile.TileEntityStellarGenerator;

public class StellarBlocks {
    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(Stellar.MODID);

    public static final BlockRegistryObject<BlockTileModel<TileEntityStellarGenerator, Generator<TileEntityStellarGenerator>>, ItemBlockMachine> STELLAR_GENERATOR = BLOCKS.register("stellar_generator", () -> new BlockTileModel<>(StellarBlockTypes.STELLAR_GENERATOR), ItemBlockMachine::new);

    public static final BlockRegistryObject<BlockTileModel<TileEntityEternalHeatGenerator, Generator<TileEntityEternalHeatGenerator>>, ItemBlockMachine> ETERNAL_HEAT_GENERATOR = BLOCKS.register("eternal_heat_generator", () -> new BlockTileModel<>(StellarBlockTypes.ETERNAL_HEAT_GENERATOR), ItemBlockMachine::new);

    private StellarBlocks() {
    }
}
