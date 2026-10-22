package net.minecraft.data.loot;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import net.minecraft.core.Holder$Reference;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.RandomSequence;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable$Builder;

class LootTableProvider$1 implements LootTableSubProvider$Context {
   LootTableProvider$1(
      final LootTableProvider this$0,
      final Map val$randomSequenceSeeds,
      final LootTableProvider$SubProviderEntry val$subProvider,
      final BootstrapContext val$context
   ) {
      this.val$randomSequenceSeeds = val$randomSequenceSeedsx;
      this.val$subProvider = val$subProviderx;
      this.val$context = val$contextx;
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public Holder$Reference<LootTable> accept(final ResourceKey<LootTable> key, final LootTable$Builder lootTable) {
      Identifier sequenceId = LootTableProvider.sequenceIdForLootTable(key);
      Identifier previous = this.val$randomSequenceSeeds.put(RandomSequence.seedForKey(sequenceId), sequenceId);
      if (previous != null) {
         Util.logAndPauseIfInIde("Loot table random sequence seed collision on " + previous + " and " + key.identifier());
      }

      LootTable table = lootTable.setRandomSequence(sequenceId).setParamSet(this.val$subProvider.paramSet).build();
      return this.val$context.register(key, table);
   }

   @Override
   public <S> HolderGetter<S> lookup(final ResourceKey<? extends Registry<? extends S>> key) {
      return this.val$context.lookup(key);
   }

   @Deprecated
   @Override
   public <S> Stream<Holder$Reference<S>> listContextElements(final ResourceKey<? extends Registry<? extends S>> key) {
      return this.val$context.listContextElements(key);
   }
}
