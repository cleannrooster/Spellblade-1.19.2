package com.cleannrooster.spellblademod.blocks;

import com.cleannrooster.spellblademod.ModBlocks;
import com.cleannrooster.spellblademod.items.ModItems;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModTileEntity {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "spellblademod");

    public static final RegistryObject<BlockEntityType<WardingTotemBlockEntity>> WARDING_TOTEM_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("warding_totem_block_entity", () ->
                    BlockEntityType.Builder.of(WardingTotemBlockEntity::new,
                            ModBlocks.WARDING_TOTEM_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<SentinelTotemBlockEntity>> SENTINEL_TOTEM_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("sentinel_totem_block_entity", () ->
                    BlockEntityType.Builder.of(SentinelTotemBlockEntity::new,
                            ModBlocks.SENTINEL_TOTEM_BLOCK.get()).build(null));




    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}