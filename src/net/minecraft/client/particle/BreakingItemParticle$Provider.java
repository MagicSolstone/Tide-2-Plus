package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class BreakingItemParticle$Provider extends BreakingItemParticle$ItemParticleProvider<ItemParticleOption> {
   public Particle createParticle(
      final ItemParticleOption options,
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final double xAux,
      final double yAux,
      final double zAux,
      final RandomSource random
   ) {
      return new BreakingItemParticle(level, x, y, z, xAux, yAux, zAux, this.getSprite(options.getItem(), level, random));
   }
}
