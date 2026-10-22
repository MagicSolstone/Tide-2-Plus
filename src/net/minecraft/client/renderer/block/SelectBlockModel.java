package net.minecraft.client.renderer.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.properties.select.SelectBlockModelProperty;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public class SelectBlockModel<T> implements BlockModel {
   private final SelectBlockModelProperty<T> property;
   private final SelectBlockModel$ModelSelector<T> models;

   public SelectBlockModel(final SelectBlockModelProperty<T> property, final SelectBlockModel$ModelSelector<T> models) {
      this.property = property;
      this.models = models;
   }

   @Override
   public void update(final BlockModelRenderState output, final BlockState blockState, final BlockDisplayContext displayContext, final long seed) {
      T value = this.property.get(blockState, displayContext);
      BlockModel model = this.models.get(value);
      if (model != null) {
         model.update(output, blockState, displayContext, seed);
      }
   }
}
