package net.minecraft.data.loot;

import net.minecraft.core.Holder$Reference;
import net.minecraft.data.worldgen.BootstrapContextAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable$Builder;

public interface LootTableSubProvider$Context extends BootstrapContextAccess {
   Holder$Reference<LootTable> accept(ResourceKey<LootTable> key, LootTable$Builder value);
}
