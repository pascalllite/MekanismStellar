package mekanism.stellar.common.provider;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

public interface ISubRecipeProvider {

    void addRecipes(Consumer<FinishedRecipe> consumer);
}
