package net.minecraft.client.renderer.block.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public class CompositeBlockModel implements BlockModel {
   private final BlockModel normal;
   private final BlockModel custom;

   public CompositeBlockModel(final BlockModel normal, final BlockModel custom) {
      this.normal = normal;
      this.custom = custom;
   }

   @Override
   public void update(final BlockModelRenderState output, final BlockState blockState, final BlockDisplayContext displayContext, final long seed) {
      this.normal.update(output, blockState, displayContext, seed);
      this.custom.update(output, blockState, displayContext, seed);
   }
}
