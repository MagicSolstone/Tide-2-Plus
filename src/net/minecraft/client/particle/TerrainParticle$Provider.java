package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class TerrainParticle$Provider implements ParticleProvider<BlockParticleOption> {
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
      return TerrainParticle.createTerrainParticle(options, level, x, y, z, xAux, yAux, zAux);
   }
}
