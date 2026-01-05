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
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.STEW_POT.get()))
                    .title(Component.translatable("creativetab.skeffy_stews_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModBlocks.STEW_POT.get());

                        //Bowls
                        pOutput.accept(ModItems.UNFIRED_CERAMIC_BOWL.get());
                        pOutput.accept(ModItems.CERAMIC_BOWL.get());
                        pOutput.accept(ModItems.GOLDEN_BOWL.get());

                        //Stew Bases
                        pOutput.accept(ModItems.STEW_BASE.get());
                        pOutput.accept(ModItems.TIER_2_STEW_BASE.get());
                        pOutput.accept(ModItems.TIER_3_STEW_BASE.get());

                        //Tier 1
                        pOutput.accept(ModItems.BLINDING_STEW.get());
                        pOutput.accept(ModItems.HEARTY_STEW.get());
                        pOutput.accept(ModItems.RABBIT_STEW.get());

                        //Tier 2
                        pOutput.accept(ModItems.FORTUNE_STEW.get());
                        pOutput.accept(ModItems.HEALING_STEW.get());

                        //Tier 3
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
