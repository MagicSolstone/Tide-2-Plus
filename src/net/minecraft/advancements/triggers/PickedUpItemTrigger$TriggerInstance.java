package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContextSource;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record PickedUpItemTrigger$TriggerInstance(
   Optional<Holder<LootItemCondition>> player, Optional<ItemPredicate> item, Optional<Holder<LootItemCondition>> entity
) implements SimpleCriterionTrigger$SimpleInstance {
   public static final Codec<PickedUpItemTrigger$TriggerInstance> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LootItemCondition.CODEC.optionalFieldOf("player").forGetter(PickedUpItemTrigger$TriggerInstance::player),
            ItemPredicate.CODEC.optionalFieldOf("item").forGetter(PickedUpItemTrigger$TriggerInstance::item),
            LootItemCondition.CODEC.optionalFieldOf("entity").forGetter(PickedUpItemTrigger$TriggerInstance::entity)
         )
         .apply(i, PickedUpItemTrigger$TriggerInstance::new)
   );

   public static Criterion<PickedUpItemTrigger$TriggerInstance> thrownItemPickedUpByEntity(
      final Optional<Holder<LootItemCondition>> player, final Optional<ItemPredicate> item, final Optional<Holder<LootItemCondition>> entity
   ) {
      return CriteriaTriggers.THROWN_ITEM_PICKED_UP_BY_ENTITY.createCriterion(new PickedUpItemTrigger$TriggerInstance(player, item, entity));
   }

   public static Criterion<PickedUpItemTrigger$TriggerInstance> thrownItemPickedUpByPlayer(
      final Optional<Holder<LootItemCondition>> player, final Optional<ItemPredicate> item, final Optional<Holder<LootItemCondition>> entity
   ) {
      return CriteriaTriggers.THROWN_ITEM_PICKED_UP_BY_PLAYER.createCriterion(new PickedUpItemTrigger$TriggerInstance(player, item, entity));
   }

   public boolean matches(final ServerPlayer player, final ItemStack itemStack, final LootContext pickedUpBy) {
      return this.item.isPresent() && !this.item.get().test((ItemInstance)itemStack)
         ? false
         : !this.entity.isPresent() || this.entity.get().value().test(pickedUpBy);
   }

   @Override
   public void validate(final ValidationContextSource validator) {
      SimpleCriterionTrigger$SimpleInstance.super.validate(validator);
      Validatable.validateHolder(validator.entityContext(), "entity", this.entity);
   }
}
