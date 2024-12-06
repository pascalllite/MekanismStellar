package mekanism.stellar.client.provider;

import mekanism.common.block.attribute.Attribute;
import mekanism.generators.common.MekanismGenerators;
import mekanism.generators.common.registries.GeneratorsBlocks;
import mekanism.stellar.common.CompressedEternalHeatGenerators;
import mekanism.stellar.common.Stellar;
import mekanism.stellar.common.registries.StellarBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;

public class StellarBlockStateProvider extends BlockStateProvider {
    private final StellarBlockModelProvider modelProvider;

    public StellarBlockStateProvider(DataGenerator gen, ExistingFileHelper existingFileHelper) {
        super(gen, Stellar.MODID, existingFileHelper);
        this.modelProvider = new StellarBlockModelProvider(gen, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        getVariantBuilder(StellarBlocks.STELLAR_GENERATOR.getBlock()).forAllStates(state -> new ConfiguredModel[]{
                new ConfiguredModel(new UncheckedModelFile(
                        MekanismGenerators.rl(GeneratorsBlocks.HEAT_GENERATOR.getName() + (Attribute.isActive(state) ? "_active" : ""))
                ))
        });
        getVariantBuilder(StellarBlocks.ETERNAL_HEAT_GENERATOR.getBlock()).forAllStates(state -> new ConfiguredModel[]{
                new ConfiguredModel(new ExistingModelFile(
                        modLoc(StellarBlocks.ETERNAL_HEAT_GENERATOR.getName()), models().existingFileHelper
                ))
        });
        for (CompressedEternalHeatGenerators type : CompressedEternalHeatGenerators.values()) {
            getVariantBuilder(StellarBlocks.COMPRESSED_ETERNAL_HEAT_GENERATORS.get(type).getBlock()).forAllStates(state -> new ConfiguredModel[]{
                    new ConfiguredModel(new ExistingModelFile(
                            modLoc(StellarBlocks.COMPRESSED_ETERNAL_HEAT_GENERATORS.get(type).getName()), models().existingFileHelper
                    ))
            });
        }
    }

    @Nonnull
    @Override
    public String getName() {
        return "Block state provider: " + Stellar.MODID;
    }

    @Override
    public StellarBlockModelProvider models() {
        return modelProvider;
    }
}
