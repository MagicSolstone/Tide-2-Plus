package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ShotCrossbowTrigger extends SimpleCriterionTrigger<ShotCrossbowTrigger$TriggerInstance> {
   @Override
   public Codec<ShotCrossbowTrigger$TriggerInstance> codec() {
      return ShotCrossbowTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final ItemStack itemStack) {
      this.trigger(player, t -> t.matches(itemStack));
   }
}
