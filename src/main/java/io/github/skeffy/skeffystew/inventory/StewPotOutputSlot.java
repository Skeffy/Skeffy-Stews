package io.github.skeffy.skeffystew.inventory;

import io.github.skeffy.skeffystew.block.entity.StewPotBlockEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class StewPotOutputSlot extends SlotItemHandler {
    private final StewPotBlockEntity entity;

    public StewPotOutputSlot(IItemHandler itemHandler, StewPotBlockEntity entity, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.entity = entity;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack pStack) {
        return false;
    }

    @Override
    public void onTake(Player pPlayer, ItemStack pStack) {
        this.checkTakeAchievements(pStack);
        if(pPlayer instanceof ServerPlayer) {
            entity.awardUsedRecipesAndPopExperience((ServerPlayer) pPlayer);
        }
        super.onTake(pPlayer, pStack);
    }
}
