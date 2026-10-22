package net.minecraft.client.renderer.block.dispatch.multipart;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel$Unbaked;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBaker$SharedOperationKey;

@Environment(EnvType.CLIENT)
class MultiPartModel$Unbaked$1 implements ModelBaker$SharedOperationKey<MultiPartModel$SharedBakedState> {
   MultiPartModel$Unbaked$1(final MultiPartModel$Unbaked this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   public MultiPartModel$SharedBakedState compute(final ModelBaker modelBakery) {
      Builder<MultiPartModel$Selector<BlockStateModel>> selectors = ImmutableList.builderWithExpectedSize(this.this$0.selectors.size());

      for (MultiPartModel$Selector<BlockStateModel$Unbaked> selector : this.this$0.selectors) {
         selectors.add(selector.with(selector.model.bake(modelBakery)));
      }

      return new MultiPartModel$SharedBakedState(selectors.build());
   }
}
