package io.github.skeffy.skeffystew.recipe;

import io.github.skeffy.skeffystew.SkeffyStews;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SkeffyStews.MOD_ID);

    public static final RegistryObject<RecipeSerializer<StewCookingRecipe>> STEW_COOKING_SERIALIZER =
            SERIALIZERS.register("stew_cooking", () -> StewCookingRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
