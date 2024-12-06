package mekanism.stellar.common.provider;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import mekanism.api.NBTConstants;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.block.BlockCardboardBox;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.attribute.AttributeUpgradeSupport;
import mekanism.common.block.attribute.Attributes.AttributeInventory;
import mekanism.common.block.attribute.Attributes.AttributeRedstone;
import mekanism.common.block.attribute.Attributes.AttributeSecurity;
import mekanism.common.block.interfaces.IHasTileEntity;
import mekanism.common.lib.frequency.IFrequencyHandler;
import mekanism.common.resource.ore.OreBlockType;
import mekanism.common.tile.base.SubstanceType;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.interfaces.ISideConfiguration;
import mekanism.common.tile.interfaces.ISustainedData;
import mekanism.common.util.EnumUtils;
import mekanism.stellar.common.registries.StellarBlocks;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

public class StellarBlockLootTables extends BlockLoot {
    private final Set<Block> knownBlocks = new ObjectOpenHashSet<>();

    private static <T> T applyExplosionCondition(boolean explosionResistant, ConditionUserBuilder<T> condition) {
        return explosionResistant ? condition.unwrap() : condition.when(ExplosionCondition.survivesExplosion());
    }

    @Nonnull
    protected static LootTable.Builder createSingleItemTable(ItemLike item) {
        return LootTable.lootTable().withPool(applyExplosionCondition(item, LootPool.lootPool()
                .name("main")
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(item))
        ));
    }

    @Override
    protected void addTables() {
        dropSelfWithContents(StellarBlocks.BLOCKS.getAllBlocks());
    }

    @Override
    protected void add(@Nonnull Block block, @Nonnull LootTable.Builder table) {
        super.add(block, table);
        knownBlocks.add(block);
    }

    @Nonnull
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return knownBlocks;
    }

    protected boolean skipBlock(Block block) {
        return knownBlocks.contains(block);
    }

    protected void add(Function<Block, Builder> factory, Collection<? extends IBlockProvider> blockProviders) {
        for (IBlockProvider blockProvider : blockProviders) {
            add(blockProvider.getBlock(), factory);
        }
    }

    protected void add(Function<Block, Builder> factory, IBlockProvider... blockProviders) {
        for (IBlockProvider blockProvider : blockProviders) {
            add(blockProvider.getBlock(), factory);
        }
    }

    protected void add(Function<Block, Builder> factory, OreBlockType... oreTypes) {
        for (OreBlockType oreType : oreTypes) {
            add(oreType.stoneBlock(), factory);
            add(oreType.deepslateBlock(), factory);
        }
    }

    protected void dropSelfWithContents(List<IBlockProvider> blockProviders) {
        for (IBlockProvider blockProvider : blockProviders) {
            Block block = blockProvider.getBlock();
            if (skipBlock(block)) {
                continue;
            }
            CopyNbtFunction.Builder nbtBuilder = CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY);
            boolean hasData = false;
            boolean hasContents = false;
            boolean isNameable = false;
            @Nullable
            BlockEntity tile = null;
            if (block instanceof IHasTileEntity<?> hasTileEntity) {
                tile = hasTileEntity.createDummyBlockEntity();
            }
            if (tile instanceof IFrequencyHandler frequencyHandler && frequencyHandler.getFrequencyComponent().hasCustomFrequencies()) {
                nbtBuilder.copy(NBTConstants.COMPONENT_FREQUENCY, NBTConstants.MEK_DATA + "." + NBTConstants.COMPONENT_FREQUENCY);
                hasData = true;
            }
            if (Attribute.has(block, AttributeSecurity.class)) {
                nbtBuilder.copy(NBTConstants.COMPONENT_SECURITY + "." + NBTConstants.OWNER_UUID, NBTConstants.MEK_DATA + "." + NBTConstants.OWNER_UUID);
                nbtBuilder.copy(NBTConstants.COMPONENT_SECURITY + "." + NBTConstants.SECURITY_MODE, NBTConstants.MEK_DATA + "." + NBTConstants.SECURITY_MODE);
                hasData = true;
            }
            if (Attribute.has(block, AttributeUpgradeSupport.class)) {
                nbtBuilder.copy(NBTConstants.COMPONENT_UPGRADE, NBTConstants.MEK_DATA + "." + NBTConstants.COMPONENT_UPGRADE);
                hasData = true;
            }
            if (tile instanceof ISideConfiguration) {
                nbtBuilder.copy(NBTConstants.COMPONENT_CONFIG, NBTConstants.MEK_DATA + "." + NBTConstants.COMPONENT_CONFIG);
                nbtBuilder.copy(NBTConstants.COMPONENT_EJECTOR, NBTConstants.MEK_DATA + "." + NBTConstants.COMPONENT_EJECTOR);
                hasData = true;
            }
            if (tile instanceof ISustainedData sustainedData) {
                Set<Entry<String, String>> remapEntries = sustainedData.getTileDataRemap().entrySet();
                for (Entry<String, String> remapEntry : remapEntries) {
                    nbtBuilder.copy(remapEntry.getKey(), NBTConstants.MEK_DATA + "." + remapEntry.getValue());
                }
                if (!remapEntries.isEmpty()) {
                    hasData = true;
                }
            }
            if (Attribute.has(block, AttributeRedstone.class)) {
                nbtBuilder.copy(NBTConstants.CONTROL_TYPE, NBTConstants.MEK_DATA + "." + NBTConstants.CONTROL_TYPE);
                hasData = true;
            }
            if (tile instanceof TileEntityMekanism tileEntity) {
                if (tileEntity.isNameable()) {
                    isNameable = true;
                }
                for (SubstanceType type : EnumUtils.SUBSTANCES) {
                    if (tileEntity.handles(type) && !type.getContainers(tileEntity).isEmpty()) {
                        nbtBuilder.copy(type.getContainerTag(), NBTConstants.MEK_DATA + "." + type.getContainerTag());
                        hasData = true;
                        if (type != SubstanceType.ENERGY && type != SubstanceType.HEAT) {
                            hasContents = true;
                        }
                    }
                }
            }
            if (Attribute.has(block, AttributeInventory.class)) {
                //If the block has an inventory, copy the inventory slots,
                // but if it is an IItemHandler, which for most cases of ours it will be,
                // then only copy the slots if we actually have any slots because otherwise maybe something just went wrong
                if (!(tile instanceof IItemHandler handler) || handler.getSlots() > 0) {
                    //If we don't actually handle saving an inventory (such as the quantum entangloporter, don't actually add it as something to copy)
                    if (!(tile instanceof TileEntityMekanism tileMek) || tileMek.persistInventory()) {
                        nbtBuilder.copy(NBTConstants.ITEMS, NBTConstants.MEK_DATA + "." + NBTConstants.ITEMS);
                        hasData = true;
                        hasContents = true;
                    }
                }
            }
            if (block instanceof BlockCardboardBox) {
                nbtBuilder.copy(NBTConstants.DATA, NBTConstants.MEK_DATA + "." + NBTConstants.DATA);
                hasData = true;
            }
            if (!hasData && !isNameable) {
                dropSelf(block);
            } else {
                LootItem.Builder<?> itemLootPool = LootItem.lootTableItem(block);
                if (isNameable) {
                    itemLootPool.apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY));
                }
                if (hasData) {
                    itemLootPool.apply(nbtBuilder);
                }
                add(block, LootTable.lootTable().withPool(applyExplosionCondition(hasContents, LootPool.lootPool()
                        .name("main")
                        .setRolls(ConstantValue.exactly(1))
                        .add(itemLootPool)
                )));
            }
        }
    }

    @Override
    public void dropOther(@Nonnull Block block, @Nonnull ItemLike drop) {
        add(block, createSingleItemTable(drop));
    }
}
