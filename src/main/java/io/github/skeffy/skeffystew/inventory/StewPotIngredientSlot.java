package io.github.skeffy.skeffystew.inventory;

import io.github.skeffy.skeffystew.screen.StewPotMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class StewPotIngredientSlot extends SlotItemHandler {
    private final StewPotMenu menu;

    public StewPotIngredientSlot(StewPotMenu menu, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.menu = menu;
    }


    @Override
    public boolean mayPlace(ItemStack pStack) {
        return this.menu.isIngredient(pStack);
    }
}
