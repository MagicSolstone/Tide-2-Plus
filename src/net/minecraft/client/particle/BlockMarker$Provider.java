package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class BlockMarker$Provider implements ParticleProvider<BlockParticleOption> {
   public Particle createParticle(
      final BlockParticleOption option,
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final double xAux,
      final double yAux,
      final double zAux,
      final RandomSource random
   ) {
      return new BlockMarker(level, x, y, z, option.getState());
   }
}
