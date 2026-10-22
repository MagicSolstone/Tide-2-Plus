package net.minecraft.client.renderer.block;

import com.mojang.math.Transformation;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.BlockModel$BakingContext;
import net.minecraft.client.renderer.block.model.BlockModel$Unbaked;
import org.joml.Matrix4fc;

@Environment(EnvType.CLIENT)
public record SelectBlockModel$Unbaked(
   Optional<Transformation> transformation, SelectBlockModel$UnbakedSwitch<?, ?> unbakedSwitch, Optional<BlockModel$Unbaked> fallback
) implements BlockModel$Unbaked {
   @Override
   public BlockModel bake(final BlockModel$BakingContext context, final Matrix4fc transformation) {
      Matrix4fc childTransform = Transformation.compose(transformation, this.transformation);
      BlockModel bakedFallback = this.fallback.<BlockModel>map(m -> m.bake(context, childTransform)).orElse(context.missingBlockModel());
      return this.unbakedSwitch.bake(context, childTransform, bakedFallback);
   }
}
