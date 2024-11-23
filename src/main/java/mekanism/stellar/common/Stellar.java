package mekanism.stellar.common;

import mekanism.common.Mekanism;
import mekanism.common.base.IModModule;
import mekanism.common.config.MekanismModConfig;
import mekanism.common.lib.Version;
import mekanism.stellar.common.config.StellarConfig;
import mekanism.stellar.common.network.StellarPacketHandler;
import mekanism.stellar.common.registries.StellarBlocks;
import mekanism.stellar.common.registries.StellarContainerTypes;
import mekanism.stellar.common.registries.StellarTileEntityTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


@Mod(Stellar.MODID)
public class Stellar implements IModModule {
    public static final String MODID = "stellar";

    public static Stellar instance;
    public final Version versionNumber;

    private final StellarPacketHandler packetHandler;

    public Stellar() {
        Mekanism.modulesLoaded.add(instance = this);
        StellarConfig.registerConfigs(ModLoadingContext.get());
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::onConfigLoad);

        StellarBlocks.BLOCKS.register(modEventBus);
        StellarContainerTypes.CONTAINER_TYPES.register(modEventBus);
        StellarTileEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);

        versionNumber = new Version(ModLoadingContext.get().getActiveContainer());
        packetHandler = new StellarPacketHandler();
    }

    public static StellarPacketHandler packetHandler() {
        return instance.packetHandler;
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(Stellar.MODID, path);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        packetHandler.initialize();
        Mekanism.logger.info("Loaded 'Mekanism Stellar' module.");
    }

    @Override
    public Version getVersion() {
        return versionNumber;
    }

    @Override
    public String getName() {
        return "Stellar";
    }

    @Override
    public void resetClient() {
    }

    private void onConfigLoad(ModConfigEvent configEvent) {
        ModConfig config = configEvent.getConfig();
        if (config.getModId().equals(MODID) && config instanceof MekanismModConfig mekConfig) {
            mekConfig.clearCache();
        }
    }
}
