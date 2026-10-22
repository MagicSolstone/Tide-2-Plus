package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.material.Fluid;

@Environment(EnvType.CLIENT)
class DripParticle$DripLandParticle extends DripParticle {
   private DripParticle$DripLandParticle(
      final ClientLevel level, final double x, final double y, final double z, final Fluid type, final TextureAtlasSprite sprite
   ) {
      super(level, x, y, z, type, sprite);
      this.lifetime = (int)(16.0 / (this.random.nextFloat() * 0.8 + 0.2));
   }
}
