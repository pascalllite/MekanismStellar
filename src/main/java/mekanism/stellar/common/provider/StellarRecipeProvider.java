package mekanism.stellar.common.provider;

import mekanism.common.registries.MekanismBlocks;
import mekanism.common.registries.MekanismItems;
import mekanism.generators.common.registries.GeneratorsBlocks;
import mekanism.stellar.common.CompressedEternalHeatGenerators;
import mekanism.stellar.common.Stellar;
import mekanism.stellar.common.registries.StellarBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.DifferenceIngredient;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class StellarRecipeProvider extends RecipeProvider {

    private final ExistingFileHelper existingFileHelper;

    public StellarRecipeProvider(DataGenerator gen, ExistingFileHelper existingFileHelper) {
        super(gen);
        this.existingFileHelper = existingFileHelper;
    }

    public static Ingredient createIngredient(TagKey<Item> itemTag, ItemLike... items) {
        return createIngredient(Collections.singleton(itemTag), items);
    }

    public static Ingredient createIngredient(Collection<TagKey<Item>> itemTags, ItemLike... items) {
        return Ingredient.fromValues(Stream.concat(
                itemTags.stream().map(Ingredient.TagValue::new),
                Arrays.stream(items).map(item -> new Ingredient.ItemValue(new ItemStack(item)))
        ));
    }

    @SafeVarargs
    public static Ingredient createIngredient(TagKey<Item>... tags) {
        return Ingredient.fromValues(Arrays.stream(tags).map(Ingredient.TagValue::new));
    }

    public static Ingredient difference(TagKey<Item> base, ItemLike subtracted) {
        return DifferenceIngredient.of(Ingredient.of(base), Ingredient.of(subtracted));
    }

    @Nonnull
    @Override
    public String getName() {
        return super.getName() + ": " + Stellar.MODID;
    }

    @Override
    protected final void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        Consumer<FinishedRecipe> trackingConsumer = consumer.andThen(recipe ->
                existingFileHelper.trackGenerated(recipe.getId(), PackType.SERVER_DATA, ".json", "recipes"));
        addRecipes(trackingConsumer);
        getSubRecipeProviders().forEach(subRecipeProvider -> subRecipeProvider.addRecipes(trackingConsumer));
    }

    protected void addRecipes(Consumer<FinishedRecipe> consumer) {
        addGeneratorRecipes(consumer);
        addCompressedEternalHeatGeneratorRecipes(consumer);
    }

    private void addGeneratorRecipes(Consumer<FinishedRecipe> consumer) {
        ExtendedShapedRecipeBuilder.shapedRecipe(StellarBlocks.STELLAR_GENERATOR)
                .pattern(RecipePattern.createPattern(
                        RecipePattern.TripleLine.of('G', 'C', 'G'),
                        RecipePattern.TripleLine.of('P', 'H', 'P'),
                        RecipePattern.TripleLine.of('G', 'C', 'G')))
                .key('H', GeneratorsBlocks.HEAT_GENERATOR)
                .key('G', MekanismItems.MODULE_GRAVITATIONAL_MODULATING)
                .key('C', MekanismBlocks.ULTIMATE_INDUCTION_CELL)
                .key('P', MekanismBlocks.ULTIMATE_INDUCTION_PROVIDER)
                .build(consumer, Stellar.rl("stellar_generator"));
        ExtendedShapedRecipeBuilder.shapedRecipe(StellarBlocks.ETERNAL_HEAT_GENERATOR)
                .pattern(RecipePattern.createPattern(
                        RecipePattern.TripleLine.of('M', 'M', 'M'),
                        RecipePattern.TripleLine.of('M', 'H', 'M'),
                        RecipePattern.TripleLine.of('M', 'M', 'M')))
                .key('H', GeneratorsBlocks.HEAT_GENERATOR)
                .key('M', Blocks.MAGMA_BLOCK)
                .build(consumer, Stellar.rl("eternal_heat_generator"));
    }

    private void addCompressedEternalHeatGeneratorRecipes(Consumer<FinishedRecipe> consumer) {
        ItemLike previous = StellarBlocks.ETERNAL_HEAT_GENERATOR;
        for (CompressedEternalHeatGenerators type : CompressedEternalHeatGenerators.values()) {
            ExtendedShapedRecipeBuilder.shapedRecipe(StellarBlocks.COMPRESSED_ETERNAL_HEAT_GENERATORS.get(type))
                    .pattern(RecipePattern.createPattern(
                            RecipePattern.TripleLine.of('P', 'P', 'P'),
                            RecipePattern.TripleLine.of('P', 'A', 'P'),
                            RecipePattern.TripleLine.of('P', 'P', 'P')))
                    .key('P', previous)
                    .key('A', MekanismItems.ATOMIC_ALLOY)
                    .build(consumer, Stellar.rl("compressed/" + type.path()));
            previous = StellarBlocks.COMPRESSED_ETERNAL_HEAT_GENERATORS.get(type);
        }
    }

    protected List<ISubRecipeProvider> getSubRecipeProviders() {
        return Collections.emptyList();
    }
}
