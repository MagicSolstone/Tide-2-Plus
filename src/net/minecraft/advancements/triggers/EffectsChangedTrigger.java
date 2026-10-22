package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jspecify.annotations.Nullable;

public class EffectsChangedTrigger extends SimpleCriterionTrigger<EffectsChangedTrigger$TriggerInstance> {
   @Override
   public Codec<EffectsChangedTrigger$TriggerInstance> codec() {
      return EffectsChangedTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, @Nullable final Entity source) {
      LootContext wrappedSource = source != null ? EntityPredicate.createContext(player, source) : null;
      this.trigger(player, t -> t.matches(player, wrappedSource));
   }
}
