package io.github.skeffy.skeffystew.compat;

import io.github.skeffy.skeffystew.SkeffyStews;
import io.github.skeffy.skeffystew.recipe.ModRecipes;
import io.github.skeffy.skeffystew.recipe.StewCookingRecipe;
import io.github.skeffy.skeffystew.screen.StewPotScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@JeiPlugin
public class JEISkeffyStewsPlugin implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(SkeffyStews.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new StewCookingCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<StewCookingRecipe> stewCookingRecipes = recipeManager.getAllRecipesFor(ModRecipes.STEW_COOKING_TYPE.get());
        registration.addRecipes(StewCookingCategory.STEW_COOKING_TYPE, stewCookingRecipes);
    }

    @Override
    public void registerGuiHandlers(@NotNull IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(StewPotScreen.class, 96, 35, 22, 24,
                StewCookingCategory.STEW_COOKING_TYPE);
    }
}
