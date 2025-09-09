package io.github.skeffy.skeffystew.item.custom;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoodProperties {
    public static final FoodProperties STEW_BASE = new FoodProperties.Builder().nutrition(2)
            .saturationMod(1f).build();
    public static final FoodProperties HEALING_STEW = new FoodProperties.Builder().nutrition(4)
            .saturationMod(1f).effect(() -> new MobEffectInstance(MobEffects.HEAL, 1), 100).build();
    public static final FoodProperties BLINDING_STEW = new FoodProperties.Builder().nutrition(4)
            .saturationMod(1f).effect(() -> new MobEffectInstance(MobEffects.BLINDNESS, 24000), 100).build();
    public static final FoodProperties HEARTY_STEW = new FoodProperties.Builder().nutrition(12)
            .saturationMod(1.5f).effect(() -> new MobEffectInstance(MobEffects.SATURATION, 1200), 100).build();
}
