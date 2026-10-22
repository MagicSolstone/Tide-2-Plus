package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class ChangeDimensionTrigger extends SimpleCriterionTrigger<ChangeDimensionTrigger$TriggerInstance> {
   @Override
   public Codec<ChangeDimensionTrigger$TriggerInstance> codec() {
      return ChangeDimensionTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final ResourceKey<Level> from, final ResourceKey<Level> to) {
      this.trigger(player, t -> t.matches(from, to));
   }
}
