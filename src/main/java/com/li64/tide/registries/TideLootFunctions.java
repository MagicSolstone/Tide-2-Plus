package com.li64.tide.registries;

import com.li64.tide.Tide;
import com.li64.tide.data.loot.ApplyFishEntityLengthFunction;
import com.li64.tide.data.loot.RandomizeFishLengthFunction;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

import java.util.HashMap;

/** In 26.x a loot function type is just its codec, registered by name. */
public class TideLootFunctions {
    public static final HashMap<String, MapCodec<? extends LootItemFunction>> LOOT_FUNCTIONS = new HashMap<>();

    public static final MapCodec<ApplyFishEntityLengthFunction> APPLY_FISH_ENTITY_LENGTH = register(
            "apply_fish_entity_length", ApplyFishEntityLengthFunction.CODEC);
    public static final MapCodec<RandomizeFishLengthFunction> RANDOMIZE_FISH_LENGTH = register(
            "randomize_fish_length", RandomizeFishLengthFunction.CODEC);

    public static <T extends LootItemFunction> MapCodec<T> register(String key, MapCodec<T> codec) {
        LOOT_FUNCTIONS.put(key, codec);
        return Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, Tide.resource(key), codec);
    }

    public static void init() {}
}
