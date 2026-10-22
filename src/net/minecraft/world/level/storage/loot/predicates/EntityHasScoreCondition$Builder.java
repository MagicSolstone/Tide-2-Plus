package net.minecraft.world.level.storage.loot.predicates;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import net.minecraft.world.level.storage.loot.IntRangePredicate;
import net.minecraft.world.level.storage.loot.LootContext$EntityTarget;

public class EntityHasScoreCondition$Builder implements LootItemCondition$Builder {
   private final Builder<String, IntRangePredicate> scores = ImmutableMap.builder();
   private final LootContext$EntityTarget entityTarget;

   public EntityHasScoreCondition$Builder(final LootContext$EntityTarget entityTarget) {
      this.entityTarget = entityTarget;
   }

   public EntityHasScoreCondition$Builder withScore(final String score, final IntRangePredicate bounds) {
      this.scores.put(score, bounds);
      return this;
   }

   @Override
   public LootItemCondition build() {
      return new EntityHasScoreCondition(this.scores.build(), this.entityTarget);
   }
}
