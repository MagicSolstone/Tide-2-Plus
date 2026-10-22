package net.minecraft.client.renderer.block.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public class EmptyBlockModel implements BlockModel {
   public static final BlockModel INSTANCE = new EmptyBlockModel();

   @Override
   public void update(final BlockModelRenderState output, final BlockState blockState, final BlockDisplayContext displayContext, final long seed) {
   }
}
