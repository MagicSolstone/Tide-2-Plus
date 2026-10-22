package net.minecraft.client.renderer.block.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.model.properties.conditional.ConditionalBlockModelProperty;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public class ConditionalBlockModel implements BlockModel {
   private final ConditionalBlockModelProperty property;
   private final BlockModel onTrue;
   private final BlockModel onFalse;

   public ConditionalBlockModel(final ConditionalBlockModelProperty property, final BlockModel onTrue, final BlockModel onFalse) {
      this.property = property;
      this.onTrue = onTrue;
      this.onFalse = onFalse;
   }

   @Override
   public void update(final BlockModelRenderState output, final BlockState blockState, final BlockDisplayContext displayContext, final long seed) {
      (this.property.get(blockState) ? this.onTrue : this.onFalse).update(output, blockState, displayContext, seed);
   }
}
