package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class FallingDustParticle$Provider implements ParticleProvider<BlockParticleOption> {
   private final SpriteSet sprite;

   public FallingDustParticle$Provider(final SpriteSet sprite) {
      this.sprite = sprite;
   }

   @Nullable
   public Particle createParticle(
      final BlockParticleOption options,
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final double xAux,
      final double yAux,
      final double zAux,
      final RandomSource random
   ) {
      BlockState blockState = options.getState();
      if (!blockState.isAir() && blockState.getRenderShape() == RenderShape.INVISIBLE) {
         return null;
      } else {
         BlockPos pos = BlockPos.containing(x, y, z);
         int tintColor;
         if (blockState.getBlock() instanceof FallingBlock fallingBlock) {
            tintColor = fallingBlock.getDustColor(blockState, level, pos);
         } else {
            BlockTintSource tintSource = Minecraft.getInstance().getBlockColors().getTintSource(blockState, 0);
            if (tintSource != null) {
               tintColor = tintSource.colorAsTerrainParticle(blockState, level, pos);
            } else {
               tintColor = blockState.getMapColor(level, pos).col;
            }
         }

         float r = (tintColor >> 16 & 0xFF) / 255.0F;
         float g = (tintColor >> 8 & 0xFF) / 255.0F;
         float b = (tintColor & 0xFF) / 255.0F;
         return new FallingDustParticle(level, x, y, z, r, g, b, this.sprite);
      }
   }
}
