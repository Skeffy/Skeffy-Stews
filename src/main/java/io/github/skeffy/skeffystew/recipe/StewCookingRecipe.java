package io.github.skeffy.skeffystew.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StewCookingRecipe extends AbstractCookingRecipe {
    private final NonNullList<Ingredient> inputItems;
    private final ItemStack result;
    private final ResourceLocation id;

    public StewCookingRecipe(ResourceLocation pId, String pGroup, CookingBookCategory pCategory, NonNullList<Ingredient> pIngredients, ItemStack pResult, float pExperience, int pCookingTime) {
        super(ModRecipes.STEW_COOKING_TYPE.get(), pId, pGroup, pCategory, pIngredients.get(0), pResult, pExperience, pCookingTime);
        this.inputItems = pIngredients;
        this.result = pResult;
        this.id = pId;
    }

    @Override
    public boolean matches(@NotNull Container pInv, @NotNull Level pLevel) {
        int[] ingredientSlots = new int[] {0,3,4};
        for(int i = 0; i < inputItems.size(); i++) {
            if(!inputItems.get(i).test(pInv.getItem(ingredientSlots[i]))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return inputItems;
    }

    @Override
    public @NotNull ItemStack getResultItem(@Nullable RegistryAccess pRegistryAccess) {
        return result.copy();
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.STEW_COOKING_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRecipes.STEW_COOKING_TYPE.get();
    }

    public static class Serializer extends SimpleCookingSerializer<StewCookingRecipe> {
        public Serializer(int pDefaultCookingTime) {
            super(StewCookingRecipe::new, pDefaultCookingTime);
        }
    }

    public static class SimpleCookingSerializer<T extends AbstractCookingRecipe> implements RecipeSerializer<T> {
        private final int defaultCookingTime;
        private final SimpleCookingSerializer.CookieBaker<T> factory;

        public SimpleCookingSerializer(SimpleCookingSerializer.CookieBaker<T> pFactory, int pDefaultCookingTime) {
            this.defaultCookingTime = pDefaultCookingTime;
            this.factory = pFactory;
        }

        public @NotNull T fromJson(@NotNull ResourceLocation pRecipeId, @NotNull JsonObject pJson) {
            String s = GsonHelper.getAsString(pJson, "group", "");
            CookingBookCategory cookingbookcategory = CookingBookCategory.CODEC.byName(GsonHelper.getAsString(pJson, "category", (String)null), CookingBookCategory.MISC);
            //JsonElement jsonelement = (JsonElement)(GsonHelper.isArrayNode(pJson, "ingredients") ? GsonHelper.getAsJsonArray(pJson, "ingredients") : GsonHelper.getAsJsonObject(pJson, "ingredients"));
            //Ingredient ingredient = Ingredient.fromJson(jsonelement, false);
            NonNullList<Ingredient> nonnulllist = itemsFromJson(GsonHelper.getAsJsonArray(pJson, "ingredients"));
            if (nonnulllist.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else if (nonnulllist.size() > 3) {
                throw new JsonParseException("Too many ingredients for shapeless recipe. The maximum is 3");
            }
            //Forge: Check if primitive string to keep vanilla or a object which can contain a count field.
            if (!pJson.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
            ItemStack itemstack;
            if (pJson.get("result").isJsonObject()) itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pJson, "result"));
            else {
                String s1 = GsonHelper.getAsString(pJson, "result");
                ResourceLocation resourcelocation = ResourceLocation.fromNamespaceAndPath(s1, s1);
                itemstack = new ItemStack(BuiltInRegistries.ITEM.getOptional(resourcelocation).orElseThrow(() -> {
                    return new IllegalStateException("Item: " + s1 + " does not exist");
                }));
            }
            float f = GsonHelper.getAsFloat(pJson, "experience", 0.0F);
            int i = GsonHelper.getAsInt(pJson, "cookingtime", this.defaultCookingTime);
            return this.factory.create(pRecipeId, s, cookingbookcategory, nonnulllist, itemstack, f, i);
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray pIngredientArray) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();

            for(int i = 0; i < pIngredientArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(pIngredientArray.get(i), false);
                nonnulllist.add(ingredient);
            }

            return nonnulllist;
        }

        public T fromNetwork(@NotNull ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            String s = pBuffer.readUtf();
            CookingBookCategory cookingbookcategory = pBuffer.readEnum(CookingBookCategory.class);
            NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);

            inputs.replaceAll(ignored -> Ingredient.fromNetwork(pBuffer));

            ItemStack output = pBuffer.readItem();
            float f = pBuffer.readFloat();
            int i = pBuffer.readVarInt();
            return this.factory.create(pRecipeId, s, cookingbookcategory, inputs, output, f, i);
        }

        public void toNetwork(FriendlyByteBuf pBuffer, T pRecipe) {
            pBuffer.writeUtf(pRecipe.getGroup());
            pBuffer.writeEnum(pRecipe.category());
            for(Ingredient ingredient : pRecipe.getIngredients()) {
                ingredient.toNetwork(pBuffer);
            }
            pBuffer.writeItem(pRecipe.getResultItem(null));
            pBuffer.writeFloat(pRecipe.getExperience());
            pBuffer.writeVarInt(pRecipe.getCookingTime());
        }

        interface CookieBaker<T extends AbstractCookingRecipe> {
            T create(ResourceLocation pId, String pGroup, CookingBookCategory pCategory, NonNullList<Ingredient> pIngredients, ItemStack pResult, float pExperience, int pCookingTime);
        }
    }
}
