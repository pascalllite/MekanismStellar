package mekanism.stellar.common.tile;

import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.math.FloatingLong;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper;
import mekanism.common.integration.computer.annotation.ComputerMethod;
import mekanism.common.integration.computer.annotation.WrappingComputerMethod;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableFloatingLong;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.util.MekanismUtils;
import mekanism.generators.common.tile.TileEntityGenerator;
import mekanism.stellar.common.CompressedEternalHeatGenerators;
import mekanism.stellar.common.config.StellarConfig;
import mekanism.stellar.common.registries.StellarBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class TileEntityCompressedEternalHeatGenerator extends TileEntityGenerator {
    public final CompressedEternalHeatGenerators type;
    private FloatingLong producingEnergy = FloatingLong.ZERO;
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getEnergyItem")
    private EnergyInventorySlot energySlot;

    public TileEntityCompressedEternalHeatGenerator(BlockPos pos, BlockState state, CompressedEternalHeatGenerators type) {
        super(StellarBlocks.COMPRESSED_ETERNAL_HEAT_GENERATORS.get(type), pos, state, StellarConfig.eternalHeatGenerator.compressedGeneration.get(type).get().multiply(2));
        this.type = type;
    }

    @Nonnull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);
        builder.addSlot(energySlot = EnergyInventorySlot.drain(getEnergyContainer(), listener, 143, 35));
        return builder.build();
    }

    @Override
    protected RelativeSide[] getEnergySides() {
        return RelativeSide.values();
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        energySlot.drainContainer();
        FloatingLong prev = getEnergyContainer().getEnergy().copyAsConst();
        if (MekanismUtils.canFunction(this)) {
            getEnergyContainer().insert(StellarConfig.eternalHeatGenerator.compressedGeneration.get(type).get(), Action.EXECUTE, AutomationType.INTERNAL);
            setActive(true);
        } else {
            setActive(false);
        }
        producingEnergy = getEnergyContainer().getEnergy().subtract(prev);
    }

    @ComputerMethod(nameOverride = "getProductionRate")
    public FloatingLong getProducingEnergy() {
        return producingEnergy;
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track((SyncableFloatingLong.create(this::getProducingEnergy, value -> producingEnergy = value)));
    }
}
