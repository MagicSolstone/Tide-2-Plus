package net.minecraft.client.particle;

import java.util.Objects;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleLimit;

@Environment(EnvType.CLIENT)
class SuspendedParticle$SporeBlossomAirProvider$1 extends SuspendedParticle {
   SuspendedParticle$SporeBlossomAirProvider$1(
      final SuspendedParticle$SporeBlossomAirProvider this$0,
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final double xd,
      final double yd,
      final double zd,
      final TextureAtlasSprite sprite
   ) {
      Objects.requireNonNull(this$0);
      super(level, x, y, z, xd, yd, zd, sprite);
   }

   @Override
   public Optional<ParticleLimit> getParticleLimit() {
      return Optional.of(ParticleLimit.SPORE_BLOSSOM);
   }
}
