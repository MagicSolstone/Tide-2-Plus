package com.li64.tide.registries;
import com.li64.tide.Tide;
import com.li64.tide.data.loot.ApplyFishEntityLengthFunction;
import com.li64.tide.data.loot.RandomizeFishLengthFunction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

import java.util.HashMap;


public class TideLootFunctions {
    public static final HashMap<String, LootItemFunctionType<?>> LOOT_FUNCTIONS = new HashMap<>();

    public static final LootItemFunctionType<ApplyFishEntityLengthFunction> APPLY_FISH_ENTITY_LENGTH = register(
            "apply_fish_entity_length", new LootItemFunctionType<>(ApplyFishEntityLengthFunction.CODEC));
    public static final LootItemFunctionType<RandomizeFishLengthFunction> RANDOMIZE_FISH_LENGTH = register(
            "randomize_fish_length", new LootItemFunctionType<>(RandomizeFishLengthFunction.CODEC));

    public static <T extends LootItemFunction> LootItemFunctionType<T> register(String key, LootItemFunctionType<T> type) {
        LOOT_FUNCTIONS.put(key, type);
        return Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, Tide.resource(key), type);
    }

    public static void init() {}
}
