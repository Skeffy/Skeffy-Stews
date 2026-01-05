package io.github.skeffy.skeffystew.item.custom;

import io.github.skeffy.skeffystew.effect.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoodProperties {
    //Stew Bases
    public static final FoodProperties STEW_BASE = new FoodProperties.Builder().alwaysEat().nutrition(2)
            .saturationMod(1f).build();
    public static final FoodProperties TIER_2_STEW_BASE = new FoodProperties.Builder().alwaysEat().nutrition(3)
            .saturationMod(1f).build();
    public static final FoodProperties TIER_3_STEW_BASE = new FoodProperties.Builder().alwaysEat().nutrition(4)
            .saturationMod(1f).build();

    //Tier 1
    public static final FoodProperties BLINDING_STEW = new FoodProperties.Builder().alwaysEat().nutrition(4)
            .saturationMod(1f).effect(() -> new MobEffectInstance(MobEffects.BLINDNESS, 24000), 100).build();
    public static final FoodProperties HEARTY_STEW = new FoodProperties.Builder().alwaysEat().nutrition(12)
            .saturationMod(1.5f).effect(() -> new MobEffectInstance(MobEffects.SATURATION, 1200), 100).build();
    public static final FoodProperties RABBIT_STEW = new FoodProperties.Builder().alwaysEat().nutrition(4)
            .saturationMod(1f).effect(() -> new MobEffectInstance(MobEffects.JUMP, 1800, 1), 100)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1800, 2), 100).build();

    //Tier 2
    public static final FoodProperties FORTUNE_STEW = new FoodProperties.Builder().alwaysEat().nutrition(4)
            .saturationMod(1f).effect(() -> new MobEffectInstance(ModEffects.FORTUNE_EFFECT.get(), 3600, 0), 100).build();
    public static final FoodProperties HEALING_STEW = new FoodProperties.Builder().alwaysEat().nutrition(4)
            .saturationMod(1f).effect(() -> new MobEffectInstance(MobEffects.HEAL, 10), 100).build();

    //Tier 3
}
