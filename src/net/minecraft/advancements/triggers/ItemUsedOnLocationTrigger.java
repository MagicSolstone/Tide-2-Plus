package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContext$Builder;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootParams$Builder;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class ItemUsedOnLocationTrigger extends SimpleCriterionTrigger<ItemUsedOnLocationTrigger$TriggerInstance> {
   @Override
   public Codec<ItemUsedOnLocationTrigger$TriggerInstance> codec() {
      return ItemUsedOnLocationTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final BlockPos pos, final ItemInstance tool) {
      ServerLevel level = player.level();
      BlockState state = level.getBlockState(pos);
      BlockEntity blockEntity = level.getBlockEntity(pos);
      LootParams params = new LootParams$Builder(level)
         .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
         .withParameter(LootContextParams.THIS_ENTITY, player)
         .withParameter(LootContextParams.BLOCK_STATE, state)
         .withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity)
         .withParameter(LootContextParams.TOOL, tool)
         .create(LootContextParamSets.ADVANCEMENT_LOCATION);
      LootContext context = new LootContext$Builder(params).create(Optional.empty());
      this.trigger(player, t -> t.matches(context));
   }
}
