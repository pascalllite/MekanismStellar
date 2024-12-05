package mekanism.stellar.client;

import mekanism.stellar.common.Stellar;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class StellarBlockStateProvider extends BaseBlockStateProvider<StellarBlockModelProvider> {
    public StellarBlockStateProvider(DataGenerator gen, ExistingFileHelper existingFileHelper) {
        super(gen, Stellar.MODID, existingFileHelper, StellarBlockModelProvider::new);
    }

    @Override
    protected void registerStatesAndModels() {
    }
}
