package mekanism.stellar.common.registries;

import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeDeferredRegister;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import mekanism.stellar.common.Stellar;
import mekanism.stellar.common.tile.TileEntityStellarGenerator;

public class StellarContainerTypes {
    public static final ContainerTypeDeferredRegister CONTAINER_TYPES = new ContainerTypeDeferredRegister(Stellar.MODID);
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityStellarGenerator>> STELLAR_GENERATOR = CONTAINER_TYPES.register(StellarBlocks.STELLAR_GENERATOR, TileEntityStellarGenerator.class);

    private StellarContainerTypes() {
    }
}
