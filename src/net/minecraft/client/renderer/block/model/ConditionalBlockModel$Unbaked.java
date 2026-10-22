package net.minecraft.client.renderer.block.model;

import com.mojang.math.Transformation;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.properties.conditional.ConditionalBlockModelProperty;
import org.joml.Matrix4fc;

@Environment(EnvType.CLIENT)
public record ConditionalBlockModel$Unbaked(
   Optional<Transformation> transformation, ConditionalBlockModelProperty property, BlockModel$Unbaked onTrue, BlockModel$Unbaked onFalse
) implements BlockModel$Unbaked {
   @Override
   public BlockModel bake(final BlockModel$BakingContext context, final Matrix4fc transformation) {
      Matrix4fc childTransform = Transformation.compose(transformation, this.transformation);
      return new ConditionalBlockModel(this.property, this.onTrue.bake(context, childTransform), this.onFalse.bake(context, childTransform));
   }
}
