package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;

public class ChanneledLightningTrigger extends SimpleCriterionTrigger<ChanneledLightningTrigger$TriggerInstance> {
   @Override
   public Codec<ChanneledLightningTrigger$TriggerInstance> codec() {
      return ChanneledLightningTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final Collection<? extends Entity> victims) {
      List<LootContext> victimsContexts = victims.stream().map(v -> EntityPredicate.createContext(player, v)).collect(Collectors.toList());
      this.trigger(player, t -> t.matches(victimsContexts));
   }
}
