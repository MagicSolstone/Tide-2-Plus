package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class FallAfterExplosionTrigger extends SimpleCriterionTrigger<FallAfterExplosionTrigger$TriggerInstance> {
   @Override
   public Codec<FallAfterExplosionTrigger$TriggerInstance> codec() {
      return FallAfterExplosionTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final Vec3 startPosition, @Nullable final Entity cause) {
      Vec3 playerPosition = player.position();
      LootContext wrappedCause = cause != null ? EntityPredicate.createContext(player, cause) : null;
      this.trigger(player, t -> t.matches(player.level(), startPosition, playerPosition, wrappedCause));
   }
}
