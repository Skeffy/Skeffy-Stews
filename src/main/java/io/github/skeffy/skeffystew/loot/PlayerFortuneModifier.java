package io.github.skeffy.skeffystew.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.skeffy.skeffystew.effect.ModEffects;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class PlayerFortuneModifier extends LootModifier {
    public static final Codec<PlayerFortuneModifier> CODEC = RecordCodecBuilder.create(inst ->
            codecStart(inst).apply(inst, PlayerFortuneModifier::new));

    public PlayerFortuneModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    @NotNull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        BlockState state = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        if (state == null || !state.is(Tags.Blocks.ORES)) {
            return generatedLoot;
        }

        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (!(entity instanceof Player player)) {
            return generatedLoot;
        }

        MobEffectInstance effect = player.getEffect(ModEffects.FORTUNE_EFFECT.get());
        if (effect == null) {
            return generatedLoot;
        }

        int fortuneLevel = effect.getAmplifier() + 1;

        ItemStack originalTool = context.getParamOrNull(LootContextParams.TOOL);
        if (originalTool == null) {
            return generatedLoot;
        }

        ItemStack fakeTool = originalTool.copy();
        var enchs = EnchantmentHelper.getEnchantments(fakeTool);

        int toolFortuneLevel = enchs.getOrDefault(Enchantments.BLOCK_FORTUNE, 0);

        int totalFortuneLevel = toolFortuneLevel + fortuneLevel;

        enchs.put(Enchantments.BLOCK_FORTUNE, totalFortuneLevel);
        EnchantmentHelper.setEnchantments(enchs, fakeTool);

        // Rebuild params with fake tool
        ServerLevel level = context.getLevel();

        LootParams.Builder builder = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, context.getParam(LootContextParams.ORIGIN))
                .withParameter(LootContextParams.BLOCK_STATE, state)
                .withParameter(LootContextParams.TOOL, fakeTool)
                .withParameter(LootContextParams.THIS_ENTITY, player)
                .withLuck(context.getLuck()); // Preserve luck from player effects

        LootParams newParams = builder.create(LootContextParamSets.BLOCK);

        // Get loot table and regenerate drops
        LootTable lootTable = level.getServer().getLootData().getLootTable(state.getBlock().getLootTable());
        ObjectArrayList<ItemStack> newLoot = new ObjectArrayList<>();
        lootTable.getRandomItemsRaw(newParams, newLoot::add);
        return newLoot;
    }
}
