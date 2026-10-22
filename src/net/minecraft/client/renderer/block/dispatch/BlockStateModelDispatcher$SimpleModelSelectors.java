package net.minecraft.client.renderer.block.dispatch;

import com.mojang.serialization.Codec;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateHolder;

@Environment(EnvType.CLIENT)
public record BlockStateModelDispatcher$SimpleModelSelectors(Map<String, BlockStateModel$Unbaked> models) {
   public static final Codec<BlockStateModelDispatcher$SimpleModelSelectors> CODEC = ExtraCodecs.nonEmptyMap(
         Codec.unboundedMap(Codec.STRING, BlockStateModel$Unbaked.CODEC)
      )
      .xmap(BlockStateModelDispatcher$SimpleModelSelectors::new, BlockStateModelDispatcher$SimpleModelSelectors::models);

   public void instantiate(
      final StateDefinition<Block, BlockState> stateDefinition, final Supplier<String> source, final BiConsumer<BlockState, BlockStateModel$UnbakedRoot> output
   ) {
      this.models.forEach((selectorString, model) -> {
         try {
            Predicate<StateHolder<Block, BlockState>> selector = VariantSelector.predicate(stateDefinition, selectorString);
            BlockStateModel$UnbakedRoot wrapper = model.asRoot();
            Iterator i$ = stateDefinition.getPossibleStates().iterator();

            while (i$.hasNext()) {
               BlockState state = (BlockState)i$.next();
               if (selector.test(state)) {
                  output.accept(state, wrapper);
               }
            }
         } catch (Exception var9) {
            BlockStateModelDispatcher.LOGGER.warn("Exception loading blockstate definition: '{}' for variant: '{}'", source.get(), selectorString);
         }
      });
   }
}
