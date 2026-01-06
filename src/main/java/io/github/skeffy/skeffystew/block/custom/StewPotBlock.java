package io.github.skeffy.skeffystew.block.custom;

import io.github.skeffy.skeffystew.block.entity.ModBlockEntities;
import io.github.skeffy.skeffystew.block.entity.StewPotBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StewPotBlock extends AbstractFurnaceBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public StewPotBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new StewPotBlockEntity(pPos, pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide()) {
            return null;
        }

        return createTickerHelper(pBlockEntityType, ModBlockEntities.STEW_POT_BLOCK_ENTITY.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1));
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState pState, Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        if(!pLevel.isClientSide) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if(entity instanceof StewPotBlockEntity) {
                NetworkHooks.openScreen(((ServerPlayer)pPlayer), (StewPotBlockEntity)entity, pPos);
            } else {
                throw new IllegalStateException("Our container provider is missing");
            }
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Override
    public void onRemove(BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if(!pState.is(pNewState.getBlock())) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if(blockEntity instanceof StewPotBlockEntity) {
                if(pLevel instanceof ServerLevel) {
                    ((StewPotBlockEntity) blockEntity).drops();
                    ((StewPotBlockEntity) blockEntity).getRecipesToAwardAndPopExperience((ServerLevel) pLevel, Vec3.atCenterOf(pPos));
                }
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }
            pLevel.removeBlockEntity(pPos);
        }
    }

    @Override
    protected void openContainer(Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if(blockEntity instanceof StewPotBlockEntity) {
            (pPlayer).openMenu(new SimpleMenuProvider((MenuProvider) blockEntity, Component.literal("Stew Pot")));
            pPlayer.awardStat(Stats.INTERACT_WITH_FURNACE);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, LIT);
    }

    @Override
    public void animateTick(BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull RandomSource pRandom) {
        if (pState.getValue(LIT)) {
            if (pRandom.nextInt(10) == 0) {
                pLevel.playLocalSound((double)pPos.getX() + 0.5D, (double)pPos.getY() + 0.5D, (double)pPos.getZ() + 0.5D, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.75F + pRandom.nextFloat(), pRandom.nextFloat() * 0.7F + 0.6F, false);
            }
        }
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        if(state.getValue(LIT)) {
            return 15;
        }
        return super.getLightEmission(state, level, pos);
    }
}
