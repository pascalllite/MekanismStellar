package mekanism.stellar.client;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;

public abstract class BaseBlockModelProvider extends BlockModelProvider {
    public BaseBlockModelProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Block model provider: " + modid;
    }
}
