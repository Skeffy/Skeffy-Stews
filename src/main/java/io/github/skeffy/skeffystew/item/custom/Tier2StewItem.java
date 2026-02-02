package io.github.skeffy.skeffystew.item.custom;

import io.github.skeffy.skeffystew.item.ModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class Tier2StewItem extends StewItem{
    public Tier2StewItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
        final ItemStack DROP_ITEM = new ItemStack(ModItems.CERAMIC_BOWL.get());
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
                    return new ItemStack(ModItems.CERAMIC_BOWL.get(), itemCount);
                }
                return DROP_ITEM;
            }
        }
        return pStack;
    }
}
