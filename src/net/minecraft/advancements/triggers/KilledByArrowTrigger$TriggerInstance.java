package net.minecraft.advancements.triggers;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.ItemPredicate$Builder;
import net.minecraft.advancements.predicates.MinMaxBounds$Ints;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate$Builder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContextSource;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jspecify.annotations.Nullable;

public record KilledByArrowTrigger$TriggerInstance(
   Optional<Holder<LootItemCondition>> player,
   List<Holder<LootItemCondition>> victims,
   MinMaxBounds$Ints uniqueEntityTypes,
   Optional<ItemPredicate> firedFromWeapon
) implements SimpleCriterionTrigger$SimpleInstance {
   public static final Codec<KilledByArrowTrigger$TriggerInstance> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LootItemCondition.CODEC.optionalFieldOf("player").forGetter(KilledByArrowTrigger$TriggerInstance::player),
            LootItemCondition.CODEC.listOf().optionalFieldOf("victims", List.of()).forGetter(KilledByArrowTrigger$TriggerInstance::victims),
            MinMaxBounds$Ints.CODEC
               .optionalFieldOf("unique_entity_types", MinMaxBounds$Ints.ANY)
               .forGetter(KilledByArrowTrigger$TriggerInstance::uniqueEntityTypes),
            ItemPredicate.CODEC.optionalFieldOf("fired_from_weapon").forGetter(KilledByArrowTrigger$TriggerInstance::firedFromWeapon)
         )
         .apply(i, KilledByArrowTrigger$TriggerInstance::new)
   );

   public static Criterion<KilledByArrowTrigger$TriggerInstance> crossbowKilled(final HolderGetter<Item> items, final EntityPredicate$Builder... victims) {
      return CriteriaTriggers.KILLED_BY_ARROW
         .createCriterion(
            new KilledByArrowTrigger$TriggerInstance(
               Optional.empty(),
               EntityPredicate.wrap(victims),
               MinMaxBounds$Ints.ANY,
               Optional.of(ItemPredicate$Builder.item().of(items, Items.CROSSBOW).build())
            )
         );
   }

   public static Criterion<KilledByArrowTrigger$TriggerInstance> crossbowKilled(final HolderGetter<Item> items, final MinMaxBounds$Ints uniqueEntityTypes) {
      return CriteriaTriggers.KILLED_BY_ARROW
         .createCriterion(
            new KilledByArrowTrigger$TriggerInstance(
               Optional.empty(), List.of(), uniqueEntityTypes, Optional.of(ItemPredicate$Builder.item().of(items, Items.CROSSBOW).build())
            )
         );
   }

   public boolean matches(final Collection<LootContext> victims, final int uniqueEntityTypes, @Nullable final ItemStack firedFromWeapon) {
      if (!this.firedFromWeapon.isPresent() || firedFromWeapon != null && this.firedFromWeapon.get().test((ItemInstance)firedFromWeapon)) {
         if (!this.victims.isEmpty()) {
            List<LootContext> victimsCopy = Lists.newArrayList(victims);

            for (Holder<LootItemCondition> predicate : this.victims) {
               boolean found = false;
               Iterator<LootContext> iterator = victimsCopy.iterator();

               while (iterator.hasNext()) {
                  LootContext entity = iterator.next();
                  if (predicate.value().test(entity)) {
                     iterator.remove();
                     found = true;
                     break;
                  }
               }

               if (!found) {
                  return false;
               }
            }
         }

         return this.uniqueEntityTypes.matches(uniqueEntityTypes);
      } else {
         return false;
      }
   }

   @Override
   public void validate(final ValidationContextSource validator) {
      SimpleCriterionTrigger$SimpleInstance.super.validate(validator);
      Validatable.validateHolder(validator.entityContext(), "victims", this.victims);
   }
}
