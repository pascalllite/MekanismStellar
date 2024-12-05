package mekanism.stellar.client;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;

public abstract class BaseBlockStateProvider<PROVIDER extends BaseBlockModelProvider> extends BlockStateProvider {

    private final String modid;
    private final PROVIDER modelProvider;

    public BaseBlockStateProvider(DataGenerator gen, String modid, ExistingFileHelper existingFileHelper, BiFunction<DataGenerator, ExistingFileHelper, PROVIDER> providerCreator) {
        super(gen, modid, existingFileHelper);
        this.modid = modid;
        this.modelProvider = providerCreator.apply(gen, existingFileHelper);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Block state provider: " + modid;
    }

    @Override
    public PROVIDER models() {
        return modelProvider;
    }
}
