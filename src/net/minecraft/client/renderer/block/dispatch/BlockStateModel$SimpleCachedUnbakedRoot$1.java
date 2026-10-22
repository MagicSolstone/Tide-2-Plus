package net.minecraft.client.renderer.block.dispatch;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBaker$SharedOperationKey;

@Environment(EnvType.CLIENT)
class BlockStateModel$SimpleCachedUnbakedRoot$1 implements ModelBaker$SharedOperationKey<BlockStateModel> {
   BlockStateModel$SimpleCachedUnbakedRoot$1(final BlockStateModel$SimpleCachedUnbakedRoot this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   public BlockStateModel compute(final ModelBaker modelBakery) {
      return this.this$0.contents.bake(modelBakery);
   }
}
