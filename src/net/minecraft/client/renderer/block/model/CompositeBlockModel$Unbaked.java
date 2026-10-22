package net.minecraft.client.renderer.block.model;

import com.mojang.math.Transformation;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Matrix4fc;

@Environment(EnvType.CLIENT)
public record CompositeBlockModel$Unbaked(BlockModel$Unbaked normal, BlockModel$Unbaked custom, Optional<Transformation> transformation)
   implements BlockModel$Unbaked {
   @Override
   public BlockModel bake(final BlockModel$BakingContext context, final Matrix4fc transformation) {
      Matrix4fc childTransform = Transformation.compose(transformation, this.transformation);
      return new CompositeBlockModel(this.normal.bake(context, childTransform), this.custom.bake(context, childTransform));
   }
}
