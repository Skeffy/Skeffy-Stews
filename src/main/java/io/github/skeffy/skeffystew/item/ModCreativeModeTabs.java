package io.github.skeffy.skeffystew.item;

import io.github.skeffy.skeffystew.SkeffyStews;
import io.github.skeffy.skeffystew.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SkeffyStews.MOD_ID);

    public static final RegistryObject<CreativeModeTab> STEWS_TAB = CREATIVE_MODE_TABS.register("skeffys_stews_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.HEALING_STEW.get()))
                    .title(Component.translatable("creativetab.skeffy_stews_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModBlocks.STEW_POT.get());
                        pOutput.accept(ModItems.BLINDING_STEW.get());
                        pOutput.accept(ModItems.HEALING_STEW.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
