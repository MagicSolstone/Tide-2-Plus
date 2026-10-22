package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.MinMaxBounds$Ints;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record ItemDurabilityTrigger$TriggerInstance(
   Optional<Holder<LootItemCondition>> player, Optional<ItemPredicate> item, MinMaxBounds$Ints durability, MinMaxBounds$Ints delta
) implements SimpleCriterionTrigger$SimpleInstance {
   public static final Codec<ItemDurabilityTrigger$TriggerInstance> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LootItemCondition.CODEC.optionalFieldOf("player").forGetter(ItemDurabilityTrigger$TriggerInstance::player),
            ItemPredicate.CODEC.optionalFieldOf("item").forGetter(ItemDurabilityTrigger$TriggerInstance::item),
            MinMaxBounds$Ints.CODEC.optionalFieldOf("durability", MinMaxBounds$Ints.ANY).forGetter(ItemDurabilityTrigger$TriggerInstance::durability),
            MinMaxBounds$Ints.CODEC.optionalFieldOf("delta", MinMaxBounds$Ints.ANY).forGetter(ItemDurabilityTrigger$TriggerInstance::delta)
         )
         .apply(i, ItemDurabilityTrigger$TriggerInstance::new)
   );

   public static Criterion<ItemDurabilityTrigger$TriggerInstance> changedDurability(final Optional<ItemPredicate> item, final MinMaxBounds$Ints durability) {
      return changedDurability(Optional.empty(), item, durability);
   }

   public static Criterion<ItemDurabilityTrigger$TriggerInstance> changedDurability(
      final Optional<Holder<LootItemCondition>> player, final Optional<ItemPredicate> item, final MinMaxBounds$Ints durability
   ) {
      return CriteriaTriggers.ITEM_DURABILITY_CHANGED
         .createCriterion(new ItemDurabilityTrigger$TriggerInstance(player, item, durability, MinMaxBounds$Ints.ANY));
   }

   public boolean matches(final ItemStack itemStack, final int newDurability) {
      if (this.item.isPresent() && !this.item.get().test((ItemInstance)itemStack)) {
         return false;
      } else {
         return !this.durability.matches(itemStack.getMaxDamage() - newDurability) ? false : this.delta.matches(itemStack.getDamageValue() - newDurability);
      }
   }
}
