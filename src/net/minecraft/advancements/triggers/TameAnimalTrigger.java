package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.storage.loot.LootContext;

public class TameAnimalTrigger extends SimpleCriterionTrigger<TameAnimalTrigger$TriggerInstance> {
   @Override
   public Codec<TameAnimalTrigger$TriggerInstance> codec() {
      return TameAnimalTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final Animal animal) {
      LootContext animalContext = EntityPredicate.createContext(player, animal);
      this.trigger(player, t -> t.matches(animalContext));
   }
}
