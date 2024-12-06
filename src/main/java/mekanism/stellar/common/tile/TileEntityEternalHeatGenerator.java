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
import mekanism.stellar.common.config.StellarConfig;
import mekanism.stellar.common.registries.StellarBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class TileEntityEternalHeatGenerator extends TileEntityGenerator {
    private FloatingLong producingEnergy = FloatingLong.ZERO;

    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getEnergyItem")
    private EnergyInventorySlot energySlot;

    public TileEntityEternalHeatGenerator(BlockPos pos, BlockState state) {
        super(StellarBlocks.ETERNAL_HEAT_GENERATOR, pos, state, StellarConfig.eternalHeatGenerator.generation.get().multiply(2));
    }

    @Nonnull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);
        builder.addSlot(energySlot = EnergyInventorySlot.drain(getEnergyContainer(), listener, 143, 35), RelativeSide.values());
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        energySlot.drainContainer();
        FloatingLong prev = getEnergyContainer().getEnergy().copyAsConst();
        if (MekanismUtils.canFunction(this)) {
            getEnergyContainer().insert(StellarConfig.eternalHeatGenerator.generation.get(), Action.EXECUTE, AutomationType.INTERNAL);
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
