package net.minecraft.client.renderer.block.model;

import com.mojang.math.Transformation;
import java.util.List;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4fc;

@Environment(EnvType.CLIENT)
public record BlockStateModelWrapper$Unbaked(BlockState model, List<BlockTintSource> tints, Optional<Transformation> transformation)
   implements BlockModel$Unbaked {
   @Override
   public BlockModel bake(final BlockModel$BakingContext context, final Matrix4fc transformation) {
      BlockStateModel baseModel = context.modelGetter().apply(this.model);
      Matrix4fc modelTransform = Transformation.compose(transformation, this.transformation);
      return new BlockStateModelWrapper(baseModel, this.tints, modelTransform);
   }
}
