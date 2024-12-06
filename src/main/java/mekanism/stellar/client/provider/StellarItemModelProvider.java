package mekanism.stellar.client.provider;

import mekanism.api.providers.IBaseProvider;
import mekanism.generators.common.MekanismGenerators;
import mekanism.generators.common.registries.GeneratorsBlocks;
import mekanism.stellar.common.CompressedEternalHeatGenerators;
import mekanism.stellar.common.Stellar;
import mekanism.stellar.common.registries.StellarBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;

public class StellarItemModelProvider extends ItemModelProvider {

    public StellarItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Stellar.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        getBuilder("item/" + StellarBlocks.STELLAR_GENERATOR.getName()).parent(new UncheckedModelFile(MekanismGenerators.rl("block/" + GeneratorsBlocks.HEAT_GENERATOR.getName())));
        registerBlock(StellarBlocks.ETERNAL_HEAT_GENERATOR);
        for (CompressedEternalHeatGenerators type : CompressedEternalHeatGenerators.values()) {
            registerBlock(StellarBlocks.COMPRESSED_ETERNAL_HEAT_GENERATORS.get(type));
        }
    }

    private void registerBlock(IBaseProvider provider) {
        getBuilder("item/" + provider.getName()).parent(new UncheckedModelFile(modLoc("block/" + provider.getName())));
    }

    @Nonnull
    @Override
    public String getName() {
        return "Item model provider: " + modid;
    }
}
