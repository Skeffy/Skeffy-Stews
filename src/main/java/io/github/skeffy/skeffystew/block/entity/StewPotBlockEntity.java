package io.github.skeffy.skeffystew.block.entity;

import io.github.skeffy.skeffystew.block.custom.StewPotBlock;
import io.github.skeffy.skeffystew.recipe.ModRecipes;
import io.github.skeffy.skeffystew.recipe.StewCookingRecipe;
import io.github.skeffy.skeffystew.screen.StewPotMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class StewPotBlockEntity extends AbstractFurnaceBlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(5);
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private static final int OUTPUT_SLOT = 4;
    private static final int BOWL_SLOT = 0;
    private static final int INGREDIENT_SLOT_1 = 1;
    private static final int INGREDIENT_SLOT_2 = 2;
    private static final int FUEL_SLOT = 3;
    int litTime;
    int litDuration;
    int cookingProgress;
    int cookingTotalTime;
    protected final ContainerData dataAccess = new ContainerData() {
        public int get(int pIndex) {
            return switch (pIndex) {
                case 0 -> StewPotBlockEntity.this.litTime;
                case 1 -> StewPotBlockEntity.this.litDuration;
                case 2 -> StewPotBlockEntity.this.cookingProgress;
                case 3 -> StewPotBlockEntity.this.cookingTotalTime;
                default -> 0;
            };
        }

        public void set(int pIndex, int pValue) {
            switch (pIndex) {
                case 0:
                    StewPotBlockEntity.this.litTime = pValue;
                    break;
                case 1:
                    StewPotBlockEntity.this.litDuration = pValue;
                    break;
                case 2:
                    StewPotBlockEntity.this.cookingProgress = pValue;
                    break;
                case 3:
                    StewPotBlockEntity.this.cookingTotalTime = pValue;
            }

        }

        public int getCount() {
            return 4;
        }
    };
    private final RecipeManager.CachedCheck<Container, ? extends AbstractCookingRecipe> quickCheck;

    public StewPotBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.STEW_POT_BLOCK_ENTITY.get(), pPos, pBlockState, ModRecipes.STEW_COOKING_TYPE.get());
        this.quickCheck = RecipeManager.createCheck((RecipeType) ModRecipes.STEW_COOKING_TYPE.get());
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for(int i =0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.skeffystews.stew_pot");
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.literal("Stew Pot");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new StewPotMenu(pContainerId, pPlayerInventory, this, this.dataAccess);
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pInventory) {
        return new StewPotMenu(pContainerId, pInventory, this, this.dataAccess);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("stew_pot.progress", cookingProgress);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        cookingProgress = pTag.getInt("stew_pot.progress");
    }

    private boolean isLit() {
        return this.litTime > 0;
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        ItemStack fuelStack = itemHandler.getStackInSlot(FUEL_SLOT);
        boolean litFlag = isLit();
        boolean updateFlag = false;
        boolean hasFuel = !fuelStack.isEmpty();
        if(isLit()) {
            --litTime;
        }

        if(!isLit() && hasFuel && hasRecipe()) {
            litTime = getBurnDuration(fuelStack);
            litDuration = litTime;
            if(isLit()) {
                updateFlag = true;
            }
            fuelStack.shrink(1);
        }

        if(isLit() && hasRecipe()) {
            cookingProgress++;
            setChanged(pLevel, pPos, pState);

            if(cookingProgress == cookingTotalTime) {
                cookingProgress = 0;
                cookingTotalTime = getTotalCookTime(pLevel, this);
                craftItem();
            }
        } else {
            cookingProgress = 0;
        }

        if(litFlag != isLit()) {
            updateFlag = true;
            pState = pState.setValue(StewPotBlock.LIT, isLit());
            pLevel.setBlock(pPos, pState, 3);
        }

        if(updateFlag) {
            setChanged(pLevel, pPos, pState);
        }
    }

    private static int getTotalCookTime(Level pLevel, StewPotBlockEntity pBlockEntity) {
        return pBlockEntity.quickCheck.getRecipeFor(pBlockEntity, pLevel).map(AbstractCookingRecipe::getCookingTime).orElse(200);
    }

    private void craftItem() {
        Optional<StewCookingRecipe> recipe = getCurrentRecipe();
        ItemStack result = recipe.get().getResultItem(null);

        this.itemHandler.extractItem(BOWL_SLOT, 1, false);
        this.itemHandler.extractItem(INGREDIENT_SLOT_1, 1, false);
        this.itemHandler.extractItem(INGREDIENT_SLOT_2, 1, false);

        this.itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(result.getItem(),
                this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()));
    }

    private boolean hasRecipe() {
        Optional<StewCookingRecipe> recipe = getCurrentRecipe();
        if(recipe.isEmpty()) {
            return false;
        }
        ItemStack result = recipe.get().getResultItem(null);
        cookingTotalTime = recipe.get().getCookingTime();
        return canInsertAmountIntoOutput(result.getCount()) && canInsertItemIntoOutput(result.getItem());
    }

    private Optional<StewCookingRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }

        return this.level.getRecipeManager().getRecipeFor(ModRecipes.STEW_COOKING_TYPE.get(), inventory, level);
    }

    private boolean canInsertItemIntoOutput(Item item) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() || this.itemHandler.getStackInSlot(OUTPUT_SLOT).is(item);
    }

    private boolean canInsertAmountIntoOutput(int count) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + count <= this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
    }
}
