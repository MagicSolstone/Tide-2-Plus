package net.minecraft.client.renderer.block.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4fc;

@Environment(EnvType.CLIENT)
public class SpecialBlockModelWrapper<T> implements BlockModel {
   private final SpecialModelRenderer<T> specialRenderer;
   private final Matrix4fc transformation;

   public SpecialBlockModelWrapper(final SpecialModelRenderer<T> specialRenderer, final Matrix4fc transformation) {
      this.specialRenderer = specialRenderer;
      this.transformation = transformation;
   }

   @Override
   public void update(final BlockModelRenderState output, final BlockState blockState, final BlockDisplayContext displayContext, final long seed) {
      output.setupSpecialModel(this.specialRenderer, this.transformation);
   }
}
