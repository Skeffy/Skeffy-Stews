package io.github.skeffy.skeffystew.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class FortuneEffect extends MobEffect {

    public FortuneEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {

    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return false;
    }
}
