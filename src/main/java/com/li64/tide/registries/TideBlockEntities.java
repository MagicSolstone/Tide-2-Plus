package com.li64.tide.registries;

import com.li64.tide.Tide;
import com.li64.tide.registries.blocks.entities.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.HashMap;

public class
TideBlockEntities {
    public static final HashMap<String, BlockEntityType<?>> BLOCK_ENTITIES = new HashMap<>();

    public static final BlockEntityType<? extends LootCrateBlockEntity> WOODEN_CRATE =
            register("wooden_crate", new BlockEntityType<>(WoodenCrateBlockEntity::new, java.util.Set.of(TideBlocks.WOODEN_CRATE)));
    public static final BlockEntityType<? extends LootCrateBlockEntity> OBSIDIAN_CRATE =
            register("obsidian_crate", new BlockEntityType<>(ObsidianCrateBlockEntity::new, java.util.Set.of(TideBlocks.OBSIDIAN_CRATE)));
    public static final BlockEntityType<? extends LootCrateBlockEntity> PURPUR_CRATE =
            register("purpur_crate", new BlockEntityType<>(PurpurCrateBlockEntity::new, java.util.Set.of(TideBlocks.PURPUR_CRATE)));

    public static final BlockEntityType<FishDisplayBlockEntity> FISH_DISPLAY =
            register("fish_display", new BlockEntityType<>(FishDisplayBlockEntity::new, java.util.Set.of(TideBlocks.FISH_DISPLAY)));

    public static <T extends BlockEntity> BlockEntityType<T> register(String key, BlockEntityType<T> block) {
        BLOCK_ENTITIES.put(key, block);
        return Tide.PLATFORM.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Tide.resource(key), block);
    }

    public static void init() {}
}
