package net.minecraft.client.renderer.block.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Matrix4fc;

@Environment(EnvType.CLIENT)
public record EmptyBlockModel$Unbaked() implements BlockModel$Unbaked {
   @Override
   public BlockModel bake(final BlockModel$BakingContext context, final Matrix4fc transformation) {
      return EmptyBlockModel.INSTANCE;
   }
}
