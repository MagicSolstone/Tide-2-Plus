package net.minecraft.server;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public class ReloadableServerRegistries$Holder {
   private final HolderLookup$Provider registries;

   public ReloadableServerRegistries$Holder(final HolderLookup$Provider registries) {
      this.registries = registries;
   }

   public HolderLookup$Provider lookup() {
      return this.registries;
   }

   public LootTable getLootTable(final ResourceKey<LootTable> id) {
      return this.registries.lookup(Registries.LOOT_TABLE).flatMap(r -> r.get(id)).map(Holder::value).orElse(LootTable.EMPTY);
   }
}
