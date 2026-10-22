package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Collection;
import java.util.Optional;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContextSource;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record FishingRodHookedTrigger$TriggerInstance(
   Optional<Holder<LootItemCondition>> player, Optional<ItemPredicate> rod, Optional<Holder<LootItemCondition>> entity, Optional<ItemPredicate> item
) implements SimpleCriterionTrigger$SimpleInstance {
   public static final Codec<FishingRodHookedTrigger$TriggerInstance> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LootItemCondition.CODEC.optionalFieldOf("player").forGetter(FishingRodHookedTrigger$TriggerInstance::player),
            ItemPredicate.CODEC.optionalFieldOf("rod").forGetter(FishingRodHookedTrigger$TriggerInstance::rod),
            LootItemCondition.CODEC.optionalFieldOf("entity").forGetter(FishingRodHookedTrigger$TriggerInstance::entity),
            ItemPredicate.CODEC.optionalFieldOf("item").forGetter(FishingRodHookedTrigger$TriggerInstance::item)
         )
         .apply(i, FishingRodHookedTrigger$TriggerInstance::new)
   );

   public static Criterion<FishingRodHookedTrigger$TriggerInstance> fishedItem(
      final Optional<ItemPredicate> rod, final Optional<EntityPredicate> entity, final Optional<ItemPredicate> item
   ) {
      return CriteriaTriggers.FISHING_ROD_HOOKED
         .createCriterion(new FishingRodHookedTrigger$TriggerInstance(Optional.empty(), rod, EntityPredicate.wrap(entity), item));
   }

   public boolean matches(final ItemStack rod, final LootContext hookedIn, final Collection<ItemStack> items) {
      if (this.rod.isPresent() && !this.rod.get().test((ItemInstance)rod)) {
         return false;
      } else if (this.entity.isPresent() && !this.entity.get().value().test(hookedIn)) {
         return false;
      } else {
         if (this.item.isPresent()) {
            boolean matched = false;
            Entity hookedInEntity = hookedIn.getOptional(LootContextParams.THIS_ENTITY);
            if (hookedInEntity instanceof ItemEntity item && this.item.get().test((ItemInstance)item.getItem())) {
               matched = true;
            }

            for (ItemStack item : items) {
               if (this.item.get().test((ItemInstance)item)) {
                  matched = true;
                  break;
               }
            }

            if (!matched) {
               return false;
            }
         }

         return true;
      }
   }

   @Override
   public void validate(final ValidationContextSource validator) {
      SimpleCriterionTrigger$SimpleInstance.super.validate(validator);
      Validatable.validateHolder(validator.entityContext(), "entity", this.entity);
   }
}
