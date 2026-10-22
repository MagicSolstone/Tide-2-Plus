package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;

@Environment(EnvType.CLIENT)
public class BreakingItemParticle$CobwebProvider extends BreakingItemParticle$ItemParticleProvider<SimpleParticleType> {
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
      return new BreakingItemParticle(level, x, y, z, this.getSprite(new ItemStackTemplate(Items.COBWEB), level, random));
   }
}
