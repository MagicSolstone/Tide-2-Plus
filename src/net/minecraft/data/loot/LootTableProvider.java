package net.minecraft.data.loot;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.SingleRegistryBootstrap;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.RandomSupport$Seed128bit;
import net.minecraft.world.level.storage.loot.LootTable;

public class LootTableProvider implements SingleRegistryBootstrap<LootTable> {
   private final Set<ResourceKey<LootTable>> requiredTables;
   private final List<LootTableProvider$SubProviderEntry> subProviders;

   public LootTableProvider(final Set<ResourceKey<LootTable>> requiredTables, final List<LootTableProvider$SubProviderEntry> subProviders) {
      this.subProviders = subProviders;
      this.requiredTables = requiredTables;
   }

   @Override
   public void run(final BootstrapContext<LootTable> context) {
      Map<RandomSupport$Seed128bit, Identifier> randomSequenceSeeds = new Object2ObjectOpenHashMap();
      HolderGetter<LootTable> lootTables = context.lookup(Registries.LOOT_TABLE);
      this.requiredTables.forEach(lootTables::get);
      this.subProviders.forEach(subProvider -> subProvider.bootstrap().create(new LootTableProvider$1(this, randomSequenceSeeds, subProvider, context)).run());
   }

   private static Identifier sequenceIdForLootTable(final ResourceKey<LootTable> id) {
      return id.identifier();
   }
}
