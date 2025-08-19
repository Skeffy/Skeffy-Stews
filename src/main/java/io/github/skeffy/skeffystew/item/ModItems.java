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
