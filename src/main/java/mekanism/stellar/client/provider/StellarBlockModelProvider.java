package mekanism.stellar.client.provider;

import mekanism.stellar.common.Stellar;
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
    }

    @Nonnull
    @Override
    public String getName() {
        return "Block model provider: " + modid;
    }
}