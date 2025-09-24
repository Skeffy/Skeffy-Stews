package io.github.skeffy.skeffystew.util;

import io.github.skeffy.skeffystew.SkeffyStews;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> BOWL_ITEMS = tag("bowl_items");
        public static final TagKey<Item> STEW_INGREDIENTS = tag("stew_ingredients");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(SkeffyStews.MOD_ID, name));
        }
    }
}
