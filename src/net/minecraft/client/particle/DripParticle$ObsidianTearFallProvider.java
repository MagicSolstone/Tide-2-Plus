package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.material.Fluids;

@Environment(EnvType.CLIENT)
public class DripParticle$ObsidianTearFallProvider implements ParticleProvider<SimpleParticleType> {
   private final SpriteSet sprite;

   public DripParticle$ObsidianTearFallProvider(final SpriteSet sprite) {
      this.sprite = sprite;
   }

   public Particle createParticle(
      final SimpleParticleType options,
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final double xAux,
      final double yAux,
      final double zAux,
      final RandomSource random
   ) {
      DripParticle particle = new DripParticle$FallAndLandParticle(level, x, y, z, Fluids.EMPTY, ParticleTypes.LANDING_OBSIDIAN_TEAR, this.sprite.get(random));
      particle.isGlowing = true;
      particle.gravity = 0.01F;
      particle.setColor(0.51171875F, 0.03125F, 0.890625F);
      return particle;
   }
}
