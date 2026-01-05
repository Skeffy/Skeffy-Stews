package io.github.skeffy.skeffystew.effect;

import io.github.skeffy.skeffystew.SkeffyStews;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, SkeffyStews.MOD_ID);

    public static final RegistryObject<MobEffect> FORTUNE_EFFECT = MOB_EFFECTS.register("player_fortune",
            () -> new FortuneEffect(MobEffectCategory.BENEFICIAL, 0xFFD700));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
