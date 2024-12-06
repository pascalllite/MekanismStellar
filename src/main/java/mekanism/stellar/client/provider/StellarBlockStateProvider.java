package mekanism.stellar.client.provider;

import mekanism.stellar.common.CompressedEternalHeatGenerators;
import mekanism.stellar.common.Stellar;
import mekanism.stellar.common.registries.StellarBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;

public class StellarBlockStateProvider extends BlockStateProvider {

    public StellarBlockStateProvider(DataGenerator gen, ExistingFileHelper existingFileHelper) {
        super(gen, Stellar.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(StellarBlocks.ETERNAL_HEAT_GENERATOR.getBlock(), models().getExistingFile(modLoc("block/" + StellarBlocks.ETERNAL_HEAT_GENERATOR.getName())));
        for (CompressedEternalHeatGenerators type : CompressedEternalHeatGenerators.values()) {
            simpleBlock(StellarBlocks.COMPRESSED_ETERNAL_HEAT_GENERATORS.get(type).getBlock(), models().getExistingFile(modLoc("block/" + StellarBlocks.COMPRESSED_ETERNAL_HEAT_GENERATORS.get(type).getName())));
        }
    }

    @Nonnull
    @Override
    public String getName() {
        return "Block state provider: " + Stellar.MODID;
    }
}
