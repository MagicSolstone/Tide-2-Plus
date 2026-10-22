package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class FilledBucketTrigger extends SimpleCriterionTrigger<FilledBucketTrigger$TriggerInstance> {
   @Override
   public Codec<FilledBucketTrigger$TriggerInstance> codec() {
      return FilledBucketTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final ItemStack item) {
      this.trigger(player, t -> t.matches(item));
   }
}
