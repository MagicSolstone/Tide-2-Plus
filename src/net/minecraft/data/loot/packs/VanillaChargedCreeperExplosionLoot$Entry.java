package net.minecraft.data.loot.packs;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootTable;

record VanillaChargedCreeperExplosionLoot$Entry(ResourceKey<LootTable> lootTable, EntityType<?> entityType, Item item) {
}
