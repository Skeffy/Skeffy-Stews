package io.github.skeffy.skeffystew.event;

import io.github.skeffy.skeffystew.SkeffyStews;
import io.github.skeffy.skeffystew.effect.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SkeffyStews.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ExperienceEventHandler {
    private static float baseMultiplier = 1.25f;

    @SubscribeEvent
    public static void onXpChange(PlayerXpEvent.XpChange event) {
        multiplyXp(event);
    }

    private static void multiplyXp(PlayerXpEvent.XpChange event) {
        Player player = event.getEntity();
        if(player.hasEffect(ModEffects.EXPERIENCE_EFFECT.get())) {
            MobEffectInstance effect = player.getEffect(ModEffects.EXPERIENCE_EFFECT.get());
            if(event.getAmount() >= 4) {
                float amplifier = effect.getAmplifier() * 0.1f;
                float multiplier = amplifier + baseMultiplier;
                event.setAmount((int) (event.getAmount() * multiplier));
            } else {
                event.setAmount(event.getAmount() + 1);
            }
        }
    }
}