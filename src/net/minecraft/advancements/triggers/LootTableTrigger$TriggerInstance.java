package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record LootTableTrigger$TriggerInstance(Optional<Holder<LootItemCondition>> player, HolderSet<LootTable> lootTable)
   implements SimpleCriterionTrigger$SimpleInstance {
   public static final Codec<LootTableTrigger$TriggerInstance> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LootItemCondition.CODEC.optionalFieldOf("player").forGetter(LootTableTrigger$TriggerInstance::player),
            LootTable.LIST_CODEC.fieldOf("loot_tables").forGetter(LootTableTrigger$TriggerInstance::lootTable)
         )
         .apply(i, LootTableTrigger$TriggerInstance::new)
   );

   public static Criterion<LootTableTrigger$TriggerInstance> lootTableUsed(final Holder<LootTable> lootTable) {
      return lootTableUsed(HolderSet.direct(lootTable));
   }

   public static Criterion<LootTableTrigger$TriggerInstance> lootTableUsed(final HolderSet<LootTable> lootTable) {
      return CriteriaTriggers.GENERATE_LOOT.createCriterion(new LootTableTrigger$TriggerInstance(Optional.empty(), lootTable));
   }

   public boolean matches(final ResourceKey<LootTable> lootTable) {
      return this.lootTable.stream().anyMatch(table -> table.is(lootTable));
   }
}
