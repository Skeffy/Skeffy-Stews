package io.github.skeffy.skeffystew.item.custom;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class StewItem extends BowlFoodItem {
    private static final int MAX_STACK_SIZE = 16;

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
        final ItemStack DROP_ITEM = new ItemStack(Items.BOWL);
        int stackSize = pStack.getCount();
        pEntityLiving.eat(pLevel, pStack);
        if(pEntityLiving instanceof Player player && !player.getAbilities().instabuild) {
            Inventory inv = player.getInventory();
            if(stackSize > 1) {
                if(inv.contains(DROP_ITEM) && inv.getItem(inv.findSlotMatchingItem(DROP_ITEM)).getCount() < 64) {
                    int slotNumber = player.getInventory().findSlotMatchingItem(DROP_ITEM);
                    player.getInventory().getItem(slotNumber).grow(1);
                } else {
                    if(player.getInventory().getFreeSlot() != -1) {
                        player.getInventory().add(DROP_ITEM);
                    } else {
                        player.drop(DROP_ITEM, false);
                    }
                }
            } else {
                if(inv.contains(DROP_ITEM) && inv.getItem(inv.findSlotMatchingItem(DROP_ITEM)).getCount() < 64) {
                    int slotNumber = player.getInventory().findSlotMatchingItem(DROP_ITEM);
                    int itemCount = inv.getItem(slotNumber).getCount() + 1;
                    inv.setItem(slotNumber, new ItemStack(Items.AIR));
                    return new ItemStack(Items.BOWL, itemCount);
                }
                return DROP_ITEM;
            }
        }
        return pStack;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return MAX_STACK_SIZE;
    }
}
