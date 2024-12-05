package mekanism.stellar.common.provider;

import mekanism.stellar.client.provider.StellarBlockStateProvider;
import mekanism.stellar.client.provider.StellarItemModelProvider;
import mekanism.stellar.client.provider.StellarLangProvider;
import mekanism.stellar.client.provider.StellarLangProviderJa;
import mekanism.stellar.common.Stellar;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Stellar.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class StellarDataGenerator {
    private StellarDataGenerator() {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (event.includeClient()) {
            gen.addProvider(new StellarLangProvider(gen));
            gen.addProvider(new StellarLangProviderJa(gen));
            StellarItemModelProvider itemModelProvider = new StellarItemModelProvider(gen, existingFileHelper);
            gen.addProvider(itemModelProvider);
            gen.addProvider(new StellarBlockStateProvider(gen, itemModelProvider.existingFileHelper));
        }

        if (event.includeServer()) {
            gen.addProvider(new StellarTagProvider(gen, existingFileHelper));
            gen.addProvider(new StellarLootProvider(gen));
            gen.addProvider(new StellarRecipeProvider(gen, existingFileHelper));
        }
    }
}