package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.GeyserParticleOptions;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class GeyserEruptionParticle$Provider implements ParticleProvider<GeyserParticleOptions> {
   @Nullable
   public Particle createParticle(
      final GeyserParticleOptions options,
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final double xAux,
      final double yAux,
      final double zAux,
      final RandomSource random
   ) {
      return new GeyserEruptionParticle(level, x, y, z, xAux, yAux, zAux, options);
   }
}
