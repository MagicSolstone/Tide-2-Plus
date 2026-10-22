package net.minecraft.client.renderer.block.dispatch;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.geometry.BakedQuad$MaterialFlags;
import net.minecraft.client.resources.model.sprite.Material$Baked;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public interface BlockStateModel {
   void collectParts(RandomSource random, List<BlockStateModelPart> output);

   Material$Baked particleMaterial();

   @BakedQuad$MaterialFlags
   int materialFlags();

   default boolean hasMaterialFlag(@BakedQuad$MaterialFlags final int flag) {
      return (this.materialFlags() & flag) != 0;
   }
}
