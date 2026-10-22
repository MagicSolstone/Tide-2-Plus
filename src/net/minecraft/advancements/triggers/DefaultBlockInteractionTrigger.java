package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContext$Builder;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootParams$Builder;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class DefaultBlockInteractionTrigger extends SimpleCriterionTrigger<DefaultBlockInteractionTrigger$TriggerInstance> {
   @Override
   public Codec<DefaultBlockInteractionTrigger$TriggerInstance> codec() {
      return DefaultBlockInteractionTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final BlockPos pos) {
      ServerLevel level = player.level();
      BlockState state = level.getBlockState(pos);
      LootParams params = new LootParams$Builder(level)
         .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
         .withParameter(LootContextParams.THIS_ENTITY, player)
         .withParameter(LootContextParams.BLOCK_STATE, state)
         .create(LootContextParamSets.BLOCK_USE);
      LootContext context = new LootContext$Builder(params).create(Optional.empty());
      this.trigger(player, t -> t.matches(context));
   }
}
