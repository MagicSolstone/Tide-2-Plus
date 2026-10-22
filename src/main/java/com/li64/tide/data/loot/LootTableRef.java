package com.li64.tide.data.loot;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.loot.LootTable;

import net.minecraft.core.registries.Registries;

public interface LootTableRef {
    default ResourceKey<LootTable> get() { return this.getKey(); }

    static LootTableRef createNew(ResourceLocation id) {
        return of(ResourceKey.create(Registries.LOOT_TABLE, id));
    }

    static LootTableRef of(ResourceKey<LootTable> key) {
        return new KeyedLootTableRef(key);
    }

    ResourceKey<LootTable> getKey();
    ResourceLocation getId();
    LootTable getTable(MinecraftServer server);
}
