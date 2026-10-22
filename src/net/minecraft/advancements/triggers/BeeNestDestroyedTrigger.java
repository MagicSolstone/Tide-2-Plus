package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BeeNestDestroyedTrigger extends SimpleCriterionTrigger<BeeNestDestroyedTrigger$TriggerInstance> {
   @Override
   public Codec<BeeNestDestroyedTrigger$TriggerInstance> codec() {
      return BeeNestDestroyedTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final BlockState state, final ItemStack itemStack, final int numBeesInside) {
      this.trigger(player, t -> t.matches(state, itemStack, numBeesInside));
   }
}
