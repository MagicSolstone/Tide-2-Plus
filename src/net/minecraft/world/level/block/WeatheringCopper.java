package net.minecraft.world.level.block;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableBiMap.Builder;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.world.level.block.state.BlockState;

public interface WeatheringCopper extends ChangeOverTimeBlock<WeatheringCopper$WeatherState> {
   Supplier<BiMap<Block, Block>> NEXT_BY_BLOCK = Suppliers.memoize(
      () -> {
         Builder<Block, Block> builder = ImmutableBiMap.builder();
         Stream.of(
               Blocks.COPPER_BLOCK,
               Blocks.CUT_COPPER,
               Blocks.CHISELED_COPPER,
               Blocks.CUT_COPPER_SLAB,
               Blocks.CUT_COPPER_STAIRS,
               Blocks.COPPER_DOOR,
               Blocks.COPPER_TRAPDOOR,
               Blocks.COPPER_BARS,
               Blocks.COPPER_GRATE,
               Blocks.COPPER_BULB,
               Blocks.COPPER_LANTERN,
               Blocks.COPPER_CHEST,
               Blocks.COPPER_GOLEM_STATUE,
               Blocks.LIGHTNING_ROD,
               Blocks.COPPER_CHAIN
            )
            .forEach(collection -> collection.weathering().progressMapping(builder::put));
         return builder.build();
      }
   );
   Supplier<BiMap<Block, Block>> PREVIOUS_BY_BLOCK = Suppliers.memoize(() -> NEXT_BY_BLOCK.get().inverse());

   static Optional<Block> getPrevious(final Block block) {
      return Optional.ofNullable((Block)PREVIOUS_BY_BLOCK.get().get(block));
   }

   static Block getFirst(final Block block) {
      Block candiate = block;

      for (Block previous = (Block)PREVIOUS_BY_BLOCK.get().get(block); previous != null; previous = (Block)PREVIOUS_BY_BLOCK.get().get(previous)) {
         candiate = previous;
      }

      return candiate;
   }

   static Optional<BlockState> getPrevious(final BlockState state) {
      return getPrevious(state.getBlock()).map(s -> s.withPropertiesOf(state));
   }

   static Optional<Block> getNext(final Block block) {
      return Optional.ofNullable((Block)NEXT_BY_BLOCK.get().get(block));
   }

   static BlockState getFirst(final BlockState state) {
      return getFirst(state.getBlock()).withPropertiesOf(state);
   }

   @Override
   default Optional<BlockState> getNext(final BlockState state) {
      return getNext(state.getBlock()).map(s -> s.withPropertiesOf(state));
   }

   @Override
   default float getChanceModifier() {
      return this.getAge() == WeatheringCopper$WeatherState.UNAFFECTED ? 0.75F : 1.0F;
   }
}
