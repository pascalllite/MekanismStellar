package mekanism.stellar.common.tile;

import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.heat.HeatAPI;
import mekanism.api.heat.IHeatHandler;
import mekanism.api.math.FloatingLong;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.capabilities.heat.BasicHeatCapacitor;
import mekanism.common.capabilities.heat.CachedAmbientTemperature;
import mekanism.common.capabilities.holder.heat.HeatCapacitorHelper;
import mekanism.common.capabilities.holder.heat.IHeatCapacitorHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper;
import mekanism.common.integration.computer.annotation.ComputerMethod;
import mekanism.common.integration.computer.annotation.WrappingComputerMethod;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableDouble;
import mekanism.common.inventory.container.sync.SyncableFloatingLong;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.util.*;
import mekanism.generators.common.tile.TileEntityGenerator;
import mekanism.stellar.common.config.StellarConfig;
import mekanism.stellar.common.registries.StellarBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class TileEntityStellarGenerator extends TileEntityGenerator {

    private double targetTemp = 300;
    private FloatingLong producingEnergy = FloatingLong.ZERO;
    private double lastTransferLoss;
    private double lastEnvironmentLoss;
    private double loss = -1;
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerHeatCapacitorWrapper.class, methodNames = "getTemperature")
    private BasicHeatCapacitor heatCapacitor;
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getEnergyItem")
    private EnergyInventorySlot energySlot;

    public TileEntityStellarGenerator(BlockPos pos, BlockState state) {
        super(StellarBlocks.STELLAR_GENERATOR, pos, state, StellarConfig.machine.energyCapacity.get());
    }

    @Nonnull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);
        builder.addSlot(energySlot = EnergyInventorySlot.drain(getEnergyContainer(), listener, 143, 35), RelativeSide.RIGHT);
        return builder.build();
    }

    @Nonnull
    @Override
    protected IHeatCapacitorHolder getInitialHeatCapacitors(IContentsListener listener, CachedAmbientTemperature ambientTemperature) {
        HeatCapacitorHelper builder = HeatCapacitorHelper.forSide(this::getDirection);
        builder.addCapacitor(heatCapacitor = BasicHeatCapacitor.create(10, 1, Double.MAX_VALUE, ambientTemperature, listener));
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        energySlot.drainContainer();
        FloatingLong prev = getEnergyContainer().getEnergy().copyAsConst();
        HeatAPI.HeatTransfer transfer = simulate();
        lastTransferLoss = transfer.adjacentTransfer();
        lastEnvironmentLoss = transfer.environmentTransfer();
        boolean active = false;
        if (MekanismUtils.canFunction(this)) {
            loss = getLoss();
            heatCapacitor.handleHeat(-loss);
            if (getEnergyContainer().getEnergy().smallerThan(getEnergyContainer().getMaxEnergy()) && loss > 0) {
                getEnergyContainer().insert(FloatingLong.create(getEnergyProduction()), Action.EXECUTE, AutomationType.INTERNAL);
                active = true;
            }
        }
        setActive(active);
        producingEnergy = getEnergyContainer().getEnergy().subtract(prev);
    }

    public double getEnergyProduction() {
        return loss * StellarConfig.machine.energyDurationMultiplier.get();
    }

    public double getLoss() {
        return Double.max(heatCapacitor.getHeat() - targetTemp * heatCapacitor.getHeatCapacity(), 0);
    }

    public double getCoolingTarget() {
        return targetTemp;
    }

    @Nonnull
    @Override
    public HeatAPI.HeatTransfer simulate() {
        double adjacentTransfer = 0;
        for (Direction side : EnumUtils.DIRECTIONS) {
            if (!canHandleHeat() || getHeatCapacitorCount(side) <= 0) {
                continue;
            }
            BlockEntity adj = WorldUtils.getTileEntity(getLevel(), getBlockPos().relative(side));
            if (adj instanceof TileEntityStellarGenerator) {
                continue;
            }
            IHeatHandler sink = CapabilityUtils.getCapability(adj, Capabilities.HEAT_HANDLER_CAPABILITY, side.getOpposite()).resolve().orElse(null);
            if (sink == null) {
                continue;
            }
            double temp = getTotalTemperature(side);
            if (temp < 0) {
                continue;
            }
            // double tempToTransfer = (temp - getAmbientTemperature(side)) / (sink.getTotalInverseConduction() + getTotalInverseConductionCoefficient(side));
            double tempToTransfer = (temp - targetTemp);
            double heatToTransfer = tempToTransfer * getTotalHeatCapacity(side);
            handleHeat(-heatToTransfer, side);
            sink.handleHeat(heatToTransfer);
            adjacentTransfer = incrementAdjacentTransfer(adjacentTransfer, tempToTransfer, side);
        }
        return new HeatAPI.HeatTransfer(adjacentTransfer, simulateEnvironment());
    }

    @ComputerMethod(nameOverride = "getProductionRate")
    public FloatingLong getProducingEnergy() {
        return producingEnergy;
    }

    @ComputerMethod(nameOverride = "getTransferLoss")
    public double getLastTransferLoss() {
        return lastTransferLoss;
    }

    @ComputerMethod(nameOverride = "getEnvironmentalLoss")
    public double getLastEnvironmentLoss() {
        return lastEnvironmentLoss;
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableFloatingLong.create(this::getProducingEnergy, value -> producingEnergy = value));
        container.track(SyncableDouble.create(this::getLastTransferLoss, value -> lastTransferLoss = value));
        container.track(SyncableDouble.create(this::getLastEnvironmentLoss, value -> lastEnvironmentLoss = value));
        container.track(SyncableDouble.create(this::getCoolingTarget, value -> targetTemp = value));
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = super.serializeNBT();
        nbt.putDouble("targetTemp", targetTemp);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        NBTUtils.setDoubleIfPresent(nbt, "targetTemp", value -> targetTemp = value);
    }

    public void setCoolingTargetFromPacket(double value) {
        if (targetTemp != value) {
            targetTemp = value;
            markForSave();
        }
    }
}
