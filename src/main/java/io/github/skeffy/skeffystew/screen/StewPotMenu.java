package io.github.skeffy.skeffystew.screen;

import io.github.skeffy.skeffystew.block.ModBlocks;
import io.github.skeffy.skeffystew.block.entity.StewPotBlockEntity;
import io.github.skeffy.skeffystew.inventory.StewPotBowlSlot;
import io.github.skeffy.skeffystew.inventory.StewPotFuelSlot;
import io.github.skeffy.skeffystew.inventory.StewPotIngredientSlot;
import io.github.skeffy.skeffystew.inventory.StewPotOutputSlot;
import io.github.skeffy.skeffystew.recipe.ModRecipes;
import io.github.skeffy.skeffystew.recipe.StewCookingRecipe;
import io.github.skeffy.skeffystew.util.ModTags;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.NotNull;

public class StewPotMenu extends AbstractContainerMenu {
    private static final int BOWL_SLOT = 0;
    private static final int FUEL_SLOT = 3;
    private static final int OUTPUT_SLOT = 4;
    private static final int INGREDIENT_SLOT_1 = 1;
    private static final int INGREDIENT_SLOT_2 = 2;
    private static final int INV_SLOT_START = 5;
    private static final int INV_SLOT_END = 31;
    private static final int HOTBAR_START = 32;
    private static final int HOTBAR_END = 41;
    private final RecipeType<StewCookingRecipe> recipeType;
    public final StewPotBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public StewPotMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(5));
    }

    public StewPotMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.STEW_POT_MENU.get(), pContainerId);
        checkContainerSize(inv, 5);
        blockEntity = ((StewPotBlockEntity) entity);
        this.level = inv.player.level();
        this.data = data;
        this.recipeType = ModRecipes.STEW_COOKING_TYPE.get();

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            this.addSlot(new StewPotBowlSlot(this, iItemHandler, BOWL_SLOT, 23, 17));
            this.addSlot(new StewPotIngredientSlot(this, iItemHandler, INGREDIENT_SLOT_1, 48, 17));
            this.addSlot(new StewPotIngredientSlot(this, iItemHandler, INGREDIENT_SLOT_2, 73, 17));
            this.addSlot(new StewPotFuelSlot(this, iItemHandler, FUEL_SLOT, 48, 53));
            this.addSlot(new StewPotOutputSlot(iItemHandler,OUTPUT_SLOT, 132, 35));
        });

        addPlayerInventory(inv);
        addPlayerHotbar(inv);
        addDataSlots(data);
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getCraftingProgress() {
        int i = this.data.get(2);
        int j = this.data.get(3);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }

    public int getFuelProgress() {
        int i = this.data.get(1);
        if (i == 0) {
            i = 200;
        }

        return this.data.get(0) * 13 / i;
    }

    public boolean isFuel(ItemStack itemStack) {
        return ForgeHooks.getBurnTime(itemStack, recipeType) > 0;
    }

    public boolean isBowl(ItemStack itemStack) {
        return itemStack.is(ModTags.Items.BOWL_ITEMS);
    }

    public boolean isIngredient(ItemStack itemStack) {
        return itemStack.is(ModTags.Items.STEW_INGREDIENTS);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (pIndex == OUTPUT_SLOT) {
                if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, HOTBAR_END, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (pIndex >= INV_SLOT_START && pIndex <= HOTBAR_END) {
                if (isBowl(itemstack1)) {
                  if (!this.moveItemStackTo(itemstack1, BOWL_SLOT, BOWL_SLOT + 1, false)) {
                      return ItemStack.EMPTY;
                  }
                } else if (isIngredient(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, INGREDIENT_SLOT_1, INGREDIENT_SLOT_2 + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (isFuel(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, FUEL_SLOT, FUEL_SLOT + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (pIndex < INV_SLOT_END) {
                    if (!this.moveItemStackTo(itemstack1, HOTBAR_START, HOTBAR_END, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (pIndex < HOTBAR_END && !this.moveItemStackTo(itemstack1, INV_SLOT_START, INV_SLOT_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, HOTBAR_END, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(pPlayer, itemstack1);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                pPlayer, ModBlocks.STEW_POT.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for(int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
