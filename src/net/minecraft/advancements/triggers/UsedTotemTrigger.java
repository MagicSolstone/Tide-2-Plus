package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class UsedTotemTrigger extends SimpleCriterionTrigger<UsedTotemTrigger$TriggerInstance> {
   @Override
   public Codec<UsedTotemTrigger$TriggerInstance> codec() {
      return UsedTotemTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final ItemStack itemStack) {
      this.trigger(player, t -> t.matches(itemStack));
   }
}
