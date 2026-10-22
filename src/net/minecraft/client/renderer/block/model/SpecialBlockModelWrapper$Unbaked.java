package net.minecraft.client.renderer.block.model;

import com.mojang.math.Transformation;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer$Unbaked;
import org.joml.Matrix4fc;

@Environment(EnvType.CLIENT)
public record SpecialBlockModelWrapper$Unbaked<T>(SpecialModelRenderer$Unbaked<T> model, Optional<Transformation> transformation) implements BlockModel$Unbaked {
   @Override
   public BlockModel bake(final BlockModel$BakingContext context, final Matrix4fc transformation) {
      SpecialModelRenderer<T> baked = this.model.bake(context);
      if (baked == null) {
         return EmptyBlockModel.INSTANCE;
      } else {
         Matrix4fc modelTransform = Transformation.compose(transformation, this.transformation);
         return new SpecialBlockModelWrapper<>(baked, modelTransform);
      }
   }
}
