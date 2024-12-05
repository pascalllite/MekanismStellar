package mekanism.stellar.common;

import mekanism.stellar.client.StellarBlockStateProvider;
import mekanism.stellar.client.StellarItemModelProvider;
import mekanism.stellar.client.StellarLangProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@EventBusSubscriber(modid = Stellar.MODID, bus = Bus.MOD)
public class StellarDataGenerator {
    private StellarDataGenerator() {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (event.includeClient()) {
            gen.addProvider(new StellarLangProvider(gen));
            StellarItemModelProvider itemModelProvider = new StellarItemModelProvider(gen, existingFileHelper);
            gen.addProvider(itemModelProvider);
            gen.addProvider(new StellarBlockStateProvider(gen, itemModelProvider.existingFileHelper));
        }

        if (event.includeServer()) {
            // gen.addProvider(new StellarTagProvider(gen, existingFileHelper));
            // gen.addProvider(new StellarLootProvider(gen));
            // gen.addProvider(new StellarRecipeProvider(gen));
        }
    }

}
