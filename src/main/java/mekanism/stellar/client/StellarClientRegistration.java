package mekanism.stellar.client;

import mekanism.client.ClientRegistrationUtil;
import mekanism.stellar.client.gui.GuiEternalHeatGenerator;
import mekanism.stellar.client.gui.GuiStellarGenerator;
import mekanism.stellar.common.Stellar;
import mekanism.stellar.common.registries.StellarBlocks;
import mekanism.stellar.common.registries.StellarContainerTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Stellar.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class StellarClientRegistration {
    private StellarClientRegistration() {
    }

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        ClientRegistrationUtil.setRenderLayer(renderType -> renderType == RenderType.solid() || renderType == RenderType.translucent(), StellarBlocks.STELLAR_GENERATOR);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void registerContainers(RegistryEvent.Register<MenuType<?>> event) {
        ClientRegistrationUtil.registerScreen(StellarContainerTypes.STELLAR_GENERATOR, GuiStellarGenerator::new);
        ClientRegistrationUtil.registerScreen(StellarContainerTypes.ETERNAL_HEAT_GENERATOR, GuiEternalHeatGenerator::new);
    }

}
