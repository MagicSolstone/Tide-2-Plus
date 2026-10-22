package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.DustParticleOptions;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class DustParticle extends DustParticleBase<DustParticleOptions> {
   protected DustParticle(
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final double xAux,
      final double yAux,
      final double zAux,
      final DustParticleOptions options,
      final SpriteSet sprites
   ) {
      super(level, x, y, z, xAux, yAux, zAux, options, sprites);
      float baseFactor = this.random.nextFloat() * 0.4F + 0.6F;
      Vector3f color = options.getColor();
      this.rCol = this.randomizeColor(color.x(), baseFactor);
      this.gCol = this.randomizeColor(color.y(), baseFactor);
      this.bCol = this.randomizeColor(color.z(), baseFactor);
   }
}
