package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jspecify.annotations.Nullable;

public class BredAnimalsTrigger extends SimpleCriterionTrigger<BredAnimalsTrigger$TriggerInstance> {
   @Override
   public Codec<BredAnimalsTrigger$TriggerInstance> codec() {
      return BredAnimalsTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final Animal parent, final Animal partner, @Nullable final AgeableMob child) {
      LootContext parentContext = EntityPredicate.createContext(player, parent);
      LootContext partnerContext = EntityPredicate.createContext(player, partner);
      LootContext childContext = child != null ? EntityPredicate.createContext(player, child) : null;
      this.trigger(player, t -> t.matches(parentContext, partnerContext, childContext));
   }
}
