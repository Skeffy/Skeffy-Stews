package io.github.skeffy.skeffystew.block.entity;

import io.github.skeffy.skeffystew.SkeffyStews;
import io.github.skeffy.skeffystew.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SkeffyStews.MOD_ID);

    public static final RegistryObject<BlockEntityType<StewPotBlockEntity>> STEW_POT_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("stew_pot_block_entity", () ->
                    BlockEntityType.Builder.of(StewPotBlockEntity::new, ModBlocks.STEW_POT.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
