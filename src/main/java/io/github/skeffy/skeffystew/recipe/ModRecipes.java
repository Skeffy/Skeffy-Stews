package io.github.skeffy.skeffystew.recipe;

import io.github.skeffy.skeffystew.SkeffyStews;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SkeffyStews.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, SkeffyStews.MOD_ID);

    public static final RegistryObject<RecipeSerializer<StewCookingRecipe>> STEW_COOKING_SERIALIZER =
            SERIALIZERS.register("stew_cooking", () -> new StewCookingRecipe.Serializer(200));
    public static final RegistryObject<RecipeType<StewCookingRecipe>> STEW_COOKING_TYPE =
            TYPES.register("stew_cooking", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return "stew_cooking";
                }
            });

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}
