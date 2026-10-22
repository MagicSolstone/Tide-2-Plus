package com.li64.tide.registries;

import com.li64.tide.Tide;
import com.li64.tide.registries.blocks.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.HashMap;
import java.util.function.Function;

public class TideBlocks {
    public static final HashMap<ResourceKey<Block>, Block> BLOCKS = new HashMap<>();

    public static Block WOODEN_CRATE = register("wooden_crate",
            WoodenCrateBlock::new, copyProperties(Blocks.BARREL));
    public static Block OBSIDIAN_CRATE = register("obsidian_crate",
            ObsidianCrateBlock::new, copyProperties(Blocks.OBSIDIAN)
                    .strength(20f, 500f));
    public static Block PURPUR_CRATE = register("purpur_crate",
            PurpurCrateBlock::new, copyProperties(Blocks.PURPUR_PILLAR)
                    .strength(2f, 8f));

    public static Block ANGLING_TABLE = register("angling_table",
            AnglingTableBlock::new, copyProperties(Blocks.CRAFTING_TABLE));
    public static Block FISH_DISPLAY = register("fish_display",
            FishDisplayBlock::new, copyProperties(Blocks.OAK_SIGN));

    public static Block JELLY_TORCH = register("jelly_torch",
            JellyTorchBlock::new, copyProperties(Blocks.TORCH).sound(SoundType.SLIME_BLOCK));
    public static Block JELLY_WALL_TORCH = register("jelly_wall_torch",
            JellyWallTorchBlock::new, copyProperties(JELLY_TORCH));

    public static Block WEATHER_RADIO = register("weather_radio",
            WeatherRadioBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .forceSolidOn()
                    .strength(0.6f)
                    .sound(SoundType.LANTERN)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY)
    );
    public static Block LUNAR_CALENDAR = register("lunar_calendar",
            LunarCalendarBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLUE)
                    .forceSolidOn()
                    .noCollission()
                    .strength(0.2f)
                    .sound(SoundType.WOOD)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
    );

    public static Block register(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties) {
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, Tide.resource(name));
        Block block = factory.apply(properties);
        BLOCKS.put(key, block);
        return Tide.PLATFORM.register(BuiltInRegistries.BLOCK, key, block);
    }

    private static BlockBehaviour.Properties copyProperties(Block copyOf) {
        return BlockBehaviour.Properties.ofFullCopy(copyOf);
    }

    public static void init() {}
}