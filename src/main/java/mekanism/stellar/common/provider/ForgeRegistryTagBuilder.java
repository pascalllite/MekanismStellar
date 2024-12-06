package mekanism.stellar.common.provider;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagKey;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ForgeRegistryTagBuilder<TYPE extends IForgeRegistryEntry<TYPE>> {

    private final Tag.Builder builder;
    private final String modID;

    public ForgeRegistryTagBuilder(Tag.Builder builder, String modID) {
        this.builder = builder;
        this.modID = modID;
    }

    @SafeVarargs
    public final ForgeRegistryTagBuilder<TYPE> add(Supplier<TYPE>... elements) {
        return addTyped(Supplier::get, elements);
    }

    @SafeVarargs
    public final ForgeRegistryTagBuilder<TYPE> add(TYPE... elements) {
        return add(IForgeRegistryEntry::getRegistryName, elements);
    }

    @SafeVarargs
    public final <T> ForgeRegistryTagBuilder<TYPE> addTyped(Function<T, TYPE> converter, T... elements) {
        return add(converter.andThen(IForgeRegistryEntry::getRegistryName), elements);
    }

    @SafeVarargs
    public final ForgeRegistryTagBuilder<TYPE> add(TagKey<TYPE>... tags) {
        return apply(rl -> builder.addTag(rl, modID), TagKey::location, tags);
    }

    public ForgeRegistryTagBuilder<TYPE> add(Tag.Entry tag) {
        builder.add(tag, modID);
        return this;
    }

    @SafeVarargs
    public final ForgeRegistryTagBuilder<TYPE> add(ResourceKey<TYPE>... keys) {
        return add(ResourceKey::location, keys);
    }

    @SafeVarargs
    public final <T> ForgeRegistryTagBuilder<TYPE> add(Function<T, ResourceLocation> locationGetter, T... elements) {
        return apply(rl -> builder.addElement(rl, modID), locationGetter, elements);
    }

    public ForgeRegistryTagBuilder<TYPE> replace() {
        return replace(true);
    }

    public ForgeRegistryTagBuilder<TYPE> replace(boolean value) {
        builder.replace(value);
        return this;
    }

    @SafeVarargs
    private <T> ForgeRegistryTagBuilder<TYPE> apply(Consumer<ResourceLocation> consumer, Function<T, ResourceLocation> locationGetter, T... elements) {
        for (T element : elements) {
            consumer.accept(locationGetter.apply(element));
        }
        return this;
    }
}