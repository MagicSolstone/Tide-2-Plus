package net.minecraft.client.renderer.block.dispatch.multipart;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.geometry.BakedQuad$MaterialFlags;
import net.minecraft.client.resources.model.sprite.Material$Baked;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class MultiPartModel implements BlockStateModel {
   private final MultiPartModel$SharedBakedState shared;
   private final BlockState blockState;
   @Nullable
   private List<BlockStateModel> models;

   private MultiPartModel(final MultiPartModel$SharedBakedState shared, final BlockState blockState) {
      this.shared = shared;
      this.blockState = blockState;
   }

   @Override
   public Material$Baked particleMaterial() {
      return this.shared.particleMaterial;
   }

   @BakedQuad$MaterialFlags
   @Override
   public int materialFlags() {
      return this.shared.materialFlags;
   }

   @Override
   public void collectParts(final RandomSource random, final List<BlockStateModelPart> output) {
      if (this.models == null) {
         this.models = this.shared.selectModels(this.blockState);
      }

      long seed = random.nextLong();

      for (BlockStateModel model : this.models) {
         random.setSeed(seed);
         model.collectParts(random, output);
      }
   }
}
