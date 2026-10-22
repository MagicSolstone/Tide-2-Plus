package net.minecraft.client.renderer.block.dispatch.multipart;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel$Unbaked;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel$UnbakedRoot;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBaker$SharedOperationKey;
import net.minecraft.client.resources.model.ResolvableModel$Resolver;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public class MultiPartModel$Unbaked implements BlockStateModel$UnbakedRoot {
   private final List<MultiPartModel$Selector<BlockStateModel$Unbaked>> selectors;
   private final ModelBaker$SharedOperationKey<MultiPartModel$SharedBakedState> sharedStateKey = new MultiPartModel$Unbaked$1(this);

   public MultiPartModel$Unbaked(final List<MultiPartModel$Selector<BlockStateModel$Unbaked>> selectors) {
      this.selectors = selectors;
   }

   @Override
   public Object visualEqualityGroup(final BlockState blockState) {
      IntList triggeredSelectors = new IntArrayList();

      for (int i = 0; i < this.selectors.size(); i++) {
         if (this.selectors.get(i).condition.test(blockState)) {
            triggeredSelectors.add(i);
         }
      }

      return new MultiPartModel$Unbaked$1Key(this, triggeredSelectors);
   }

   @Override
   public void resolveDependencies(final ResolvableModel$Resolver resolver) {
      this.selectors.forEach(s -> s.model.resolveDependencies(resolver));
   }

   @Override
   public BlockStateModel bake(final BlockState blockState, final ModelBaker modelBakery) {
      MultiPartModel$SharedBakedState shared = modelBakery.compute(this.sharedStateKey);
      return new MultiPartModel(shared, blockState);
   }
}
