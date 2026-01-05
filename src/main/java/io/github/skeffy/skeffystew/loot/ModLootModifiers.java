package io.github.skeffy.skeffystew.loot;

import com.mojang.serialization.Codec;
import io.github.skeffy.skeffystew.SkeffyStews;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModLootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, SkeffyStews.MOD_ID);

    public static final RegistryObject<Codec<PlayerFortuneModifier>> PLAYER_FORTUNE_MODIFIER =
            LOOT_MODIFIER_SERIALIZERS.register("player_fortune_modifier", () -> PlayerFortuneModifier.CODEC);

    public static void register(IEventBus eventBus) {
        LOOT_MODIFIER_SERIALIZERS.register(eventBus);
    }
}
