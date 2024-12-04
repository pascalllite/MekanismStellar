package mekanism.stellar.common.registries;

import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.stellar.common.Stellar;
import mekanism.stellar.common.tile.TileEntityEternalHeatGenerator;
import mekanism.stellar.common.tile.TileEntityStellarGenerator;

public class StellarTileEntityTypes {
    public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(Stellar.MODID);

    public static final TileEntityTypeRegistryObject<TileEntityStellarGenerator> STELLAR_GENERATOR = TILE_ENTITY_TYPES.register(StellarBlocks.STELLAR_GENERATOR, TileEntityStellarGenerator::new);

    public static final TileEntityTypeRegistryObject<TileEntityEternalHeatGenerator> ETERNAL_HEAT_GENERATOR = TILE_ENTITY_TYPES.register(StellarBlocks.ETERNAL_HEAT_GENERATOR, TileEntityEternalHeatGenerator::new);

    private StellarTileEntityTypes() {
    }
}
