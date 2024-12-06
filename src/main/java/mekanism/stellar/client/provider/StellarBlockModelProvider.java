package mekanism.stellar.client.provider;

import mekanism.stellar.common.CompressedEternalHeatGenerators;
import mekanism.stellar.common.Stellar;
import mekanism.stellar.common.registries.StellarBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;

public class StellarBlockModelProvider extends BlockModelProvider {

    public StellarBlockModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Stellar.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        var original = modLoc("block/" + StellarBlocks.ETERNAL_HEAT_GENERATOR.getName());
        for (CompressedEternalHeatGenerators type : CompressedEternalHeatGenerators.values()) {
            withExistingParent(StellarBlocks.COMPRESSED_ETERNAL_HEAT_GENERATORS.get(type).getName(), original);
        }
    }

    @Nonnull
    @Override
    public String getName() {
        return "Block model provider: " + modid;
    }
}
