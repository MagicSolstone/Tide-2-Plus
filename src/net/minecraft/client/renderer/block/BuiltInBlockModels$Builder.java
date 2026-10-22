package net.minecraft.client.renderer.block;

import com.google.common.collect.UnmodifiableIterator;
import java.util.HashMap;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.model.BlockModel$Unbaked;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
class BuiltInBlockModels$Builder {
   private final BlockColors blockColors;
   private final Map<BlockState, BlockModel$Unbaked> result = new HashMap<>();

   private BuiltInBlockModels$Builder(final BlockColors blockColors) {
      this.blockColors = blockColors;
   }

   private void put(final BuiltInBlockModels$ModelFactory factory, final Block a, final Block b) {
      this.put(factory, a);
      this.put(factory, b);
   }

   private void put(final BlockModel$Unbaked specialModel, final Block block) {
      this.put((BuiltInBlockModels$SpecialModelFactory)var1 -> specialModel, block);
   }

   private void put(final BuiltInBlockModels$ModelFactory factory, final Block block) {
      UnmodifiableIterator var3 = block.getStateDefinition().getPossibleStates().iterator();

      while (var3.hasNext()) {
         BlockState blockState = (BlockState)var3.next();
         this.result.put(blockState, factory.create(this.blockColors, blockState));
      }
   }

   public Map<BlockState, BlockModel$Unbaked> build() {
      return Map.copyOf(this.result);
   }
}
