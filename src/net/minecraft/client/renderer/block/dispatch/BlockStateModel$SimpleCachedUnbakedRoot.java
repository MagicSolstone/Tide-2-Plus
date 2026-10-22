package net.minecraft.client.renderer.block.dispatch;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBaker$SharedOperationKey;
import net.minecraft.client.resources.model.ResolvableModel$Resolver;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public class BlockStateModel$SimpleCachedUnbakedRoot implements BlockStateModel$UnbakedRoot {
   private final BlockStateModel$Unbaked contents;
   private final ModelBaker$SharedOperationKey<BlockStateModel> bakingKey = new BlockStateModel$SimpleCachedUnbakedRoot$1(this);

   public BlockStateModel$SimpleCachedUnbakedRoot(final BlockStateModel$Unbaked contents) {
      this.contents = contents;
   }

   @Override
   public void resolveDependencies(final ResolvableModel$Resolver resolver) {
      this.contents.resolveDependencies(resolver);
   }

   @Override
   public BlockStateModel bake(final BlockState blockState, final ModelBaker modelBakery) {
      return modelBakery.compute(this.bakingKey);
   }

   @Override
   public Object visualEqualityGroup(final BlockState blockState) {
      return this;
   }
}
