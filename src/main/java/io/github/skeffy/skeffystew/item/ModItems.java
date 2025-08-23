package io.github.skeffy.skeffystew.item;

import io.github.skeffy.skeffystew.SkeffyStews;
import io.github.skeffy.skeffystew.item.custom.ModFoodProperties;
import io.github.skeffy.skeffystew.item.custom.StewItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;


public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SkeffyStews.MOD_ID);

    public static List<RegistryObject<Item>> modItems = new ArrayList<>();

    public static final RegistryObject<Item> UNFIRED_CERAMIC_BOWL = ITEMS.register("unfired_ceramic_bowl",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CERAMIC_BOWL = ITEMS.register("ceramic_bowl",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> STEW_BASE = ITEMS.register("stew_base",
            () -> new StewItem(new StewItem.Properties().food(ModFoodProperties.STEW_BASE)));
    public static final RegistryObject<Item> BLINDING_STEW = ITEMS.register("blinding_stew",
            () -> new StewItem(new StewItem.Properties().food(ModFoodProperties.BLINDING_STEW)));
    public static final RegistryObject<Item> HEALING_STEW = ITEMS.register("healing_stew",
            () -> new StewItem(new StewItem.Properties().food(ModFoodProperties.HEALING_STEW)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static List<RegistryObject<Item>> getAll() {
        return modItems;
    }
}
