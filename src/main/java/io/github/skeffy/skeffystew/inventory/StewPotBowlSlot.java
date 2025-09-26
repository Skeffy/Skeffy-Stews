package io.github.skeffy.skeffystew.inventory;

import io.github.skeffy.skeffystew.screen.StewPotMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class StewPotBowlSlot extends SlotItemHandler {
    private final StewPotMenu menu;

    public StewPotBowlSlot(StewPotMenu menu, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.menu = menu;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack pStack) {
        return this.menu.isBowl(pStack);
    }
}
