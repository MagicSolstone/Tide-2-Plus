package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.ItemPredicate$Builder;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate$Builder;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record UsingItemTrigger$TriggerInstance(Optional<Holder<LootItemCondition>> player, Optional<ItemPredicate> item)
   implements SimpleCriterionTrigger$SimpleInstance {
   public static final Codec<UsingItemTrigger$TriggerInstance> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LootItemCondition.CODEC.optionalFieldOf("player").forGetter(UsingItemTrigger$TriggerInstance::player),
            ItemPredicate.CODEC.optionalFieldOf("item").forGetter(UsingItemTrigger$TriggerInstance::item)
         )
         .apply(i, UsingItemTrigger$TriggerInstance::new)
   );

   public static Criterion<UsingItemTrigger$TriggerInstance> lookingAt(final EntityPredicate$Builder player, final ItemPredicate$Builder with) {
      return CriteriaTriggers.USING_ITEM
         .createCriterion(new UsingItemTrigger$TriggerInstance(Optional.of(EntityPredicate.wrap(player)), Optional.of(with.build())));
   }

   public boolean matches(final ItemStack item) {
      return !this.item.isPresent() || this.item.get().test((ItemInstance)item);
   }
}
