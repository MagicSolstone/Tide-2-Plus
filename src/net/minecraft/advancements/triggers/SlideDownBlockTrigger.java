package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;

public class SlideDownBlockTrigger extends SimpleCriterionTrigger<SlideDownBlockTrigger$TriggerInstance> {
   @Override
   public Codec<SlideDownBlockTrigger$TriggerInstance> codec() {
      return SlideDownBlockTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final BlockState state) {
      this.trigger(player, t -> t.matches(state));
   }
}
