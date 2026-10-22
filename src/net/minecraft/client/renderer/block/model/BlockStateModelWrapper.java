package net.minecraft.client.renderer.block.model;

import it.unimi.dsi.fastutil.ints.IntList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4fc;

@Environment(EnvType.CLIENT)
public class BlockStateModelWrapper implements BlockModel {
   private final BlockStateModel model;
   private final List<BlockTintSource> tints;
   private final Matrix4fc transformation;

   public BlockStateModelWrapper(final BlockStateModel model, final List<BlockTintSource> tints, final Matrix4fc transformation) {
      this.model = model;
      this.tints = tints;
      this.transformation = transformation;
   }

   @Override
   public void update(final BlockModelRenderState output, final BlockState blockState, final BlockDisplayContext displayContext, final long seed) {
      List<BlockStateModelPart> partList = output.setupModel(this.transformation, this.model.hasMaterialFlag(1));
      this.model.collectParts(output.scratchRandomSource(seed), partList);
      this.updateTints(output, blockState);
   }

   private void updateTints(final BlockModelRenderState renderState, final BlockState blockState) {
      if (!this.tints.isEmpty()) {
         IntList tintLayers = renderState.tintLayers();

         for (BlockTintSource tint : this.tints) {
            tintLayers.add(tint.color(blockState));
         }
      }
   }
}
