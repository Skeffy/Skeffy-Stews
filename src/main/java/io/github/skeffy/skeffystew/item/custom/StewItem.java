package io.github.skeffy.skeffystew.item.custom;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class StewItem extends BowlFoodItem {
    private static final int MAX_STACK_SIZE = 1;

    public StewItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isEdible() {
        return true;
    }

    @Override
    public @Nullable FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
        return super.getFoodProperties(stack, entity);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
        return super.finishUsingItem(pStack, pLevel, pEntityLiving);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return MAX_STACK_SIZE;
    }
}
