package io.github.skeffy.skeffystew.compat;

import io.github.skeffy.skeffystew.SkeffyStews;
import io.github.skeffy.skeffystew.block.ModBlocks;
import io.github.skeffy.skeffystew.recipe.StewCookingRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StewCookingCategory implements IRecipeCategory<StewCookingRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(SkeffyStews.MOD_ID, "stew_cooking");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SkeffyStews.MOD_ID,
            "textures/gui/stew_pot.png");

    public static final RecipeType<StewCookingRecipe> STEW_COOKING_TYPE =
            new RecipeType<>(UID, StewCookingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public StewCookingCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.STEW_POT.get()));
    }

    @Override
    public @NotNull RecipeType<StewCookingRecipe> getRecipeType() {
        return STEW_COOKING_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("block.skeffystews.stew_pot");
    }

    @Override
    public @Nullable IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, StewCookingRecipe recipe, IFocusGroup iFocusGroup) {
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 23, 17).addIngredients(recipe.getIngredients().get(0));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 48, 17).addIngredients(recipe.getIngredients().get(1));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 73, 17).addIngredients(recipe.getIngredients().get(2));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.CATALYST, 48, 53).addIngredients(Ingredient.of(Items.COAL));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 132, 35).addItemStack(recipe.getResultItem(null));
    }
}
