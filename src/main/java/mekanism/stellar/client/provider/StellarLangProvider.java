package mekanism.stellar.client.provider;

import mekanism.api.gear.ModuleData;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.providers.IModuleDataProvider;
import mekanism.api.text.IHasTranslationKey;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.attribute.AttributeGui;
import mekanism.stellar.common.CompressedEternalHeatGenerators;
import mekanism.stellar.common.Stellar;
import mekanism.stellar.common.StellarLang;
import mekanism.stellar.common.registries.StellarBlocks;
import net.minecraft.Util;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;

public class StellarLangProvider extends LanguageProvider {

    private final ConvertibleLanguageProvider[] altProviders;

    public StellarLangProvider(DataGenerator gen) {
        super(gen, Stellar.MODID, "en_us");
        altProviders = new ConvertibleLanguageProvider[]{
                new UpsideDownLanguageProvider(gen, Stellar.MODID),
        };
    }

    @Override
    protected void addTranslations() {
        addBlocks();
        addMisc();
    }

    public void addBlocks() {
        add(StellarBlocks.STELLAR_GENERATOR, "Stellar Generator");
        add(StellarBlocks.ETERNAL_HEAT_GENERATOR, "Eternal Heat Generator");

        for (CompressedEternalHeatGenerators type : CompressedEternalHeatGenerators.values()) {
            add(StellarBlocks.COMPRESSED_ETERNAL_HEAT_GENERATORS.get(type), type.si() + " Compressed Eternal Heat Generator");
        }
    }

    public void addMisc() {
        add(StellarLang.HEAT_LOSS, "Heat Loss: %1$s/t");
        add(StellarLang.COOLING_TARGET, "Cooling Target: %1$s");

        add(StellarLang.DESCRIPTION_STELLAR_GENERATOR, "Get even hotter and hotter and hotter and HOTTTTTTTTTTTTTTTTTTTTTTTTTTTTTER!!!!!!!!");
        add(StellarLang.DESCRIPTION_ETERNAL_HEAT_GENERATOR, "Maxwell's intelligent incommensurable demon");

        for (CompressedEternalHeatGenerators type : CompressedEternalHeatGenerators.values()) {
            add(StellarLang.DESCRIPTION_COMPRESSED_ETERNAL_HEAT_GENERATOR.type(type), type.description());
        }
    }

    @Nonnull
    @Override
    public String getName() {
        return super.getName() + ": " + Stellar.MODID;
    }

    protected void add(IHasTranslationKey key, String value) {
        if (key instanceof IBlockProvider blockProvider) {
            Block block = blockProvider.getBlock();
            if (Attribute.has(block, AttributeGui.class) && !Attribute.get(block, AttributeGui.class).hasCustomName()) {
                add(Util.makeDescriptionId("container", block.getRegistryName()), value);
            }
        }
        add(key.getTranslationKey(), value);
    }

    protected void add(IBlockProvider blockProvider, String value, String containerName) {
        Block block = blockProvider.getBlock();
        if (Attribute.has(block, AttributeGui.class) && !Attribute.get(block, AttributeGui.class).hasCustomName()) {
            add(Util.makeDescriptionId("container", block.getRegistryName()), containerName);
            add(blockProvider.getTranslationKey(), value);
        } else {
            throw new IllegalArgumentException("Block " + block.getRegistryName() + " does not have a container name set.");
        }
    }

    protected void add(IModuleDataProvider<?> moduleDataProvider, String name, String description) {
        ModuleData<?> moduleData = moduleDataProvider.getModuleData();
        add(moduleData.getTranslationKey(), name);
        add(moduleData.getDescriptionTranslationKey(), description);
    }

    @Override
    public void add(@Nonnull String key, @Nonnull String value) {
        if (value.contains("%s")) {
            throw new IllegalArgumentException("Values containing substitutions should use explicit numbered indices: " + key + " - " + value);
        }
        super.add(key, value);
        if (altProviders.length > 0) {
            List<FormatSplitter.Component> splitEnglish = FormatSplitter.split(value);
            for (ConvertibleLanguageProvider provider : altProviders) {
                provider.convert(key, splitEnglish);
            }
        }
    }

    @Override
    public void run(@Nonnull HashCache cache) throws IOException {
        super.run(cache);
        for (ConvertibleLanguageProvider provider : altProviders) {
            provider.run(cache);
        }
    }
}
