package io.github.skeffy.skeffystew.compat;

import io.github.skeffy.skeffystew.SkeffyStews;
import io.github.skeffy.skeffystew.block.ModBlocks;
import io.github.skeffy.skeffystew.recipe.StewCookingRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.placement.VerticalAlignment;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
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
    private final int regularCookTime = 200;

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
    public void createRecipeExtras(IRecipeExtrasBuilder builder, StewCookingRecipe recipe, IFocusGroup focuses) {
        int cookTime = recipe.getCookingTime();
        if (cookTime <= 0) {
            cookTime = regularCookTime;
        }
        builder.addAnimatedRecipeArrow(cookTime)
                .setPosition(97, 34);
        builder.addAnimatedRecipeFlame(300)
                .setPosition(48, 36);

        addExperience(builder, recipe);
        addCookTime(builder, recipe);
    }

    protected void addExperience(IRecipeExtrasBuilder builder, StewCookingRecipe recipe) {
        float experience = recipe.getExperience();
        if (experience > 0) {
            Component experienceString = Component.translatable("gui.jei.category.smelting.experience", experience);
            builder.addText(experienceString, getWidth() - 20, 10)
                    .setPosition(0, 10)
                    .setTextAlignment(HorizontalAlignment.RIGHT)
                    .setColor(0xFF808080);
        }
    }

    protected void addCookTime(IRecipeExtrasBuilder builder, StewCookingRecipe recipe) {
        int cookTime = recipe.getCookingTime();
        if (cookTime <= 0) {
            cookTime = regularCookTime;
        }
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
            builder.addText(timeString, getWidth() - 20, 10)
                    .setPosition(0, 65)
                    .setTextAlignment(HorizontalAlignment.RIGHT)
                    .setTextAlignment(VerticalAlignment.BOTTOM)
                    .setColor(0xFF808080);
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, StewCookingRecipe recipe, IFocusGroup iFocusGroup) {
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 23, 17).addIngredients(recipe.getIngredients().get(0));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 48, 17).addIngredients(recipe.getIngredients().get(1));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 73, 17).addIngredients(recipe.getIngredients().get(2));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 132, 35).addItemStack(recipe.getResultItem(null));
    }
}
