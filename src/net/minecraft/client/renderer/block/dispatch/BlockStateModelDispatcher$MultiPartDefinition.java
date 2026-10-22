package net.minecraft.client.renderer.block.dispatch;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.mojang.serialization.Codec;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.dispatch.multipart.MultiPartModel$Selector;
import net.minecraft.client.renderer.block.dispatch.multipart.MultiPartModel$Unbaked;
import net.minecraft.client.renderer.block.dispatch.multipart.Selector;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

@Environment(EnvType.CLIENT)
public record BlockStateModelDispatcher$MultiPartDefinition(List<Selector> selectors) {
   public static final Codec<BlockStateModelDispatcher$MultiPartDefinition> CODEC = ExtraCodecs.nonEmptyList(Selector.CODEC.listOf())
      .xmap(BlockStateModelDispatcher$MultiPartDefinition::new, BlockStateModelDispatcher$MultiPartDefinition::selectors);

   public MultiPartModel$Unbaked instantiate(final StateDefinition<Block, BlockState> stateDefinition) {
      Builder<MultiPartModel$Selector<BlockStateModel$Unbaked>> instantiatedSelectors = ImmutableList.builderWithExpectedSize(this.selectors.size());

      for (Selector selector : this.selectors) {
         instantiatedSelectors.add(new MultiPartModel$Selector<>(selector.instantiate(stateDefinition), selector.variant()));
      }

      return new MultiPartModel$Unbaked(instantiatedSelectors.build());
   }
}
