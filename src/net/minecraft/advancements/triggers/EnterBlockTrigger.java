package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;

public class EnterBlockTrigger extends SimpleCriterionTrigger<EnterBlockTrigger$TriggerInstance> {
   @Override
   public Codec<EnterBlockTrigger$TriggerInstance> codec() {
      return EnterBlockTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final BlockState state) {
      this.trigger(player, t -> t.matches(state));
   }
}
