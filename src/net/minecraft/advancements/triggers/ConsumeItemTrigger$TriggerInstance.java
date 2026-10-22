package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.ItemPredicate$Builder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record ConsumeItemTrigger$TriggerInstance(Optional<Holder<LootItemCondition>> player, Optional<ItemPredicate> item)
   implements SimpleCriterionTrigger$SimpleInstance {
   public static final Codec<ConsumeItemTrigger$TriggerInstance> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LootItemCondition.CODEC.optionalFieldOf("player").forGetter(ConsumeItemTrigger$TriggerInstance::player),
            ItemPredicate.CODEC.optionalFieldOf("item").forGetter(ConsumeItemTrigger$TriggerInstance::item)
         )
         .apply(i, ConsumeItemTrigger$TriggerInstance::new)
   );

   public static Criterion<ConsumeItemTrigger$TriggerInstance> usedItem() {
      return CriteriaTriggers.CONSUME_ITEM.createCriterion(new ConsumeItemTrigger$TriggerInstance(Optional.empty(), Optional.empty()));
   }

   public static Criterion<ConsumeItemTrigger$TriggerInstance> usedItem(final HolderGetter<Item> items, final ItemLike item) {
      return usedItem(ItemPredicate$Builder.item().of(items, item.asItem()));
   }

   public static Criterion<ConsumeItemTrigger$TriggerInstance> usedItem(final ItemPredicate$Builder predicate) {
      return CriteriaTriggers.CONSUME_ITEM.createCriterion(new ConsumeItemTrigger$TriggerInstance(Optional.empty(), Optional.of(predicate.build())));
   }

   public boolean matches(final ItemStack itemStack) {
      return this.item.isEmpty() || this.item.get().test((ItemInstance)itemStack);
   }
}
