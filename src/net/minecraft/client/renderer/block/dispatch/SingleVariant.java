package net.minecraft.client.renderer.block.dispatch;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.geometry.BakedQuad$MaterialFlags;
import net.minecraft.client.resources.model.sprite.Material$Baked;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class SingleVariant implements BlockStateModel {
   private final BlockStateModelPart model;

   public SingleVariant(final BlockStateModelPart model) {
      this.model = model;
   }

   @Override
   public void collectParts(final RandomSource random, final List<BlockStateModelPart> output) {
      output.add(this.model);
   }

   @Override
   public Material$Baked particleMaterial() {
      return this.model.particleMaterial();
   }

   @BakedQuad$MaterialFlags
   @Override
   public int materialFlags() {
      return this.model.materialFlags();
   }
}
