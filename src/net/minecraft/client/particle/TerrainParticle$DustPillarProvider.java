package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class TerrainParticle$DustPillarProvider implements ParticleProvider<BlockParticleOption> {
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
      Particle particle = TerrainParticle.createTerrainParticle(options, level, x, y, z, xAux, yAux, zAux);
      if (particle != null) {
         particle.setParticleSpeed(random.nextGaussian() / 30.0, yAux + random.nextGaussian() / 2.0, random.nextGaussian() / 30.0);
         particle.setLifetime(random.nextInt(20) + 20);
      }

      return particle;
   }
}
