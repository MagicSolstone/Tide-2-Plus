package com.li64.tide.data.loot;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.loot.LootTable;

public record KeyedLootTableRef(ResourceKey<LootTable> key) implements LootTableRef {
    @Override
    public ResourceKey<LootTable> getKey() {
        return this.key;
    }

    @Override
    public ResourceLocation getId() {
        return this.key.location();
    }

    @Override
    public LootTable getTable(MinecraftServer server) {
        return server.reloadableRegistries().getLootTable(this.key);
    }
}