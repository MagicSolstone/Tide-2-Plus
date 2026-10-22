package net.minecraft.client.renderer.block.model.properties.conditional;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public class IsXmas implements ConditionalBlockModelProperty {
   @Override
   public boolean get(final BlockState blockState) {
      return ChestRenderer.xmasTextures();
   }
}
