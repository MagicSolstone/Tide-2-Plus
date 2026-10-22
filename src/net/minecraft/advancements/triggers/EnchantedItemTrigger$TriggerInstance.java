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

public record EnchantedItemTrigger$TriggerInstance(Optional<Holder<LootItemCondition>> player, Optional<ItemPredicate> item, MinMaxBounds$Ints levels)
   implements SimpleCriterionTrigger$SimpleInstance {
   public static final Codec<EnchantedItemTrigger$TriggerInstance> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LootItemCondition.CODEC.optionalFieldOf("player").forGetter(EnchantedItemTrigger$TriggerInstance::player),
            ItemPredicate.CODEC.optionalFieldOf("item").forGetter(EnchantedItemTrigger$TriggerInstance::item),
            MinMaxBounds$Ints.CODEC.optionalFieldOf("levels", MinMaxBounds$Ints.ANY).forGetter(EnchantedItemTrigger$TriggerInstance::levels)
         )
         .apply(i, EnchantedItemTrigger$TriggerInstance::new)
   );

   public static Criterion<EnchantedItemTrigger$TriggerInstance> enchantedItem() {
      return CriteriaTriggers.ENCHANTED_ITEM
         .createCriterion(new EnchantedItemTrigger$TriggerInstance(Optional.empty(), Optional.empty(), MinMaxBounds$Ints.ANY));
   }

   public boolean matches(final ItemStack itemStack, final int levels) {
      return this.item.isPresent() && !this.item.get().test((ItemInstance)itemStack) ? false : this.levels.matches(levels);
   }
}
