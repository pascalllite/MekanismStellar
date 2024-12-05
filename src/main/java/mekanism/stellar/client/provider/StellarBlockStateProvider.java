package mekanism.stellar.client.provider;

import mekanism.stellar.common.Stellar;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
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
