package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.ItemPredicate$Builder;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record FilledBucketTrigger$TriggerInstance(Optional<Holder<LootItemCondition>> player, Optional<ItemPredicate> item)
   implements SimpleCriterionTrigger$SimpleInstance {
   public static final Codec<FilledBucketTrigger$TriggerInstance> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LootItemCondition.CODEC.optionalFieldOf("player").forGetter(FilledBucketTrigger$TriggerInstance::player),
            ItemPredicate.CODEC.optionalFieldOf("item").forGetter(FilledBucketTrigger$TriggerInstance::item)
         )
         .apply(i, FilledBucketTrigger$TriggerInstance::new)
   );

   public static Criterion<FilledBucketTrigger$TriggerInstance> filledBucket(final ItemPredicate$Builder item) {
      return CriteriaTriggers.FILLED_BUCKET.createCriterion(new FilledBucketTrigger$TriggerInstance(Optional.empty(), Optional.of(item.build())));
   }

   public boolean matches(final ItemStack item) {
      return !this.item.isPresent() || this.item.get().test((ItemInstance)item);
   }
}
