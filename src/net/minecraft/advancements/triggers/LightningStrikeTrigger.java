package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.storage.loot.LootContext;

public class LightningStrikeTrigger extends SimpleCriterionTrigger<LightningStrikeTrigger$TriggerInstance> {
   @Override
   public Codec<LightningStrikeTrigger$TriggerInstance> codec() {
      return LightningStrikeTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final LightningBolt lightning, final List<Entity> entitiesAround) {
      List<LootContext> entitiesAroundContexts = entitiesAround.stream().map(v -> EntityPredicate.createContext(player, v)).collect(Collectors.toList());
      LootContext lightningContext = EntityPredicate.createContext(player, lightning);
      this.trigger(player, t -> t.matches(lightningContext, entitiesAroundContexts));
   }
}
