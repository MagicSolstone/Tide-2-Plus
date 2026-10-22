package net.minecraft.client.renderer.block.model.properties.select;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public record DisplayContext() implements SelectBlockModelProperty<BlockDisplayContext> {
   public BlockDisplayContext get(final BlockState blockState, final BlockDisplayContext displayContext) {
      return displayContext;
   }
}
