package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ConsumeItemTrigger extends SimpleCriterionTrigger<ConsumeItemTrigger$TriggerInstance> {
   @Override
   public Codec<ConsumeItemTrigger$TriggerInstance> codec() {
      return ConsumeItemTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final ItemStack itemStack) {
      this.trigger(player, t -> t.matches(itemStack));
   }
}
