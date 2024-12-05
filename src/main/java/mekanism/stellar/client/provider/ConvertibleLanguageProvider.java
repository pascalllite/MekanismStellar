package mekanism.stellar.client.provider;

import mekanism.stellar.client.provider.FormatSplitter.Component;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.List;

public abstract class ConvertibleLanguageProvider extends LanguageProvider {
    public ConvertibleLanguageProvider(DataGenerator gen, String modid, String locale) {
        super(gen, modid, locale);
    }

    public abstract void convert(String key, List<Component> splitEnglish);

    @Override
    protected void addTranslations() {

    }
}
