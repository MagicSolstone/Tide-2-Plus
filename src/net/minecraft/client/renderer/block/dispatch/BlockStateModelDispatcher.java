package net.minecraft.client.renderer.block.dispatch;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public record BlockStateModelDispatcher(
   Optional<BlockStateModelDispatcher$SimpleModelSelectors> simpleModels, Optional<BlockStateModelDispatcher$MultiPartDefinition> multiPart
) {
   private static final Logger LOGGER = LogUtils.getLogger();
   public static final Codec<BlockStateModelDispatcher> CODEC = RecordCodecBuilder.create(
         i -> i.group(
               BlockStateModelDispatcher$SimpleModelSelectors.CODEC.optionalFieldOf("variants").forGetter(BlockStateModelDispatcher::simpleModels),
               BlockStateModelDispatcher$MultiPartDefinition.CODEC.optionalFieldOf("multipart").forGetter(BlockStateModelDispatcher::multiPart)
            )
            .apply(i, BlockStateModelDispatcher::new)
      )
      .validate(
         o -> o.simpleModels().isEmpty() && o.multiPart().isEmpty()
            ? DataResult.error(() -> "Neither 'variants' nor 'multipart' found")
            : DataResult.success(o)
      );

   public Map<BlockState, BlockStateModel$UnbakedRoot> instantiate(final StateDefinition<Block, BlockState> stateDefinition, final Supplier<String> source) {
      Map<BlockState, BlockStateModel$UnbakedRoot> matchedStates = new IdentityHashMap<>();
      this.simpleModels.ifPresent(s -> s.instantiate(stateDefinition, source, (state, model) -> {
         BlockStateModel$UnbakedRoot previousValue = matchedStates.put(state, model);
         if (previousValue != null) {
            throw new IllegalArgumentException("Overlapping definition on state: " + state);
         }
      }));
      this.multiPart.ifPresent(m -> {
         List<BlockState> possibleStates = stateDefinition.getPossibleStates();
         BlockStateModel$UnbakedRoot model = m.instantiate(stateDefinition);

         for (BlockState state : possibleStates) {
            matchedStates.putIfAbsent(state, model);
         }
      });
      return matchedStates;
   }
}
