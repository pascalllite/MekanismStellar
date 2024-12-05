package mekanism.stellar.client;

import mekanism.api.providers.IBlockProvider;
import mekanism.client.ClientRegistrationUtil;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.stellar.client.gui.GuiCompressedEternalHeatGenerator;
import mekanism.stellar.client.gui.GuiEternalHeatGenerator;
import mekanism.stellar.client.gui.GuiStellarGenerator;
import mekanism.stellar.common.CompressedEternalHeatGenerators;
import mekanism.stellar.common.Stellar;
import mekanism.stellar.common.registries.StellarBlocks;
import mekanism.stellar.common.registries.StellarContainerTypes;
import mekanism.stellar.common.tile.TileEntityCompressedEternalHeatGenerator;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Stellar.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class StellarClientRegistration {
    private StellarClientRegistration() {
    }

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        List<IBlockProvider> objects = new ArrayList<>();
        objects.add(StellarBlocks.STELLAR_GENERATOR);
        objects.add(StellarBlocks.ETERNAL_HEAT_GENERATOR);
        for (CompressedEternalHeatGenerators type : CompressedEternalHeatGenerators.values()) {
            objects.add(StellarBlocks.COMPRESSED_ETERNAL_HEAT_GENERATORS.get(type));
        }
        ClientRegistrationUtil.setRenderLayer(renderType -> renderType == RenderType.solid() || renderType == RenderType.translucent(), objects.toArray(IBlockProvider[]::new));
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void registerContainers(RegistryEvent.Register<MenuType<?>> event) {
        ClientRegistrationUtil.registerScreen(StellarContainerTypes.STELLAR_GENERATOR, GuiStellarGenerator::new);
        ClientRegistrationUtil.registerScreen(StellarContainerTypes.ETERNAL_HEAT_GENERATOR, GuiEternalHeatGenerator::new);
        for (CompressedEternalHeatGenerators type : CompressedEternalHeatGenerators.values()) {
            ClientRegistrationUtil.registerScreen(StellarContainerTypes.COMPRESSED_ETERNAL_HEAT_GENERATORS.get(type), (MekanismTileContainer<TileEntityCompressedEternalHeatGenerator> container, Inventory inv, Component title) -> new GuiCompressedEternalHeatGenerator(container, inv, title, type));
        }
    }

}
