package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;

@Environment(EnvType.CLIENT)
class FireworkParticles$SparkParticle extends SimpleAnimatedParticle {
   private boolean trail;
   private boolean twinkle;
   private final ParticleEngine engine;
   private float fadeR;
   private float fadeG;
   private float fadeB;
   private boolean hasFade;

   private FireworkParticles$SparkParticle(
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final double xa,
      final double ya,
      final double za,
      final ParticleEngine engine,
      final SpriteSet sprites
   ) {
      super(level, x, y, z, sprites, 0.1F);
      this.xd = xa;
      this.yd = ya;
      this.zd = za;
      this.engine = engine;
      this.quadSize *= 0.75F;
      this.lifetime = 48 + this.random.nextInt(12);
      this.setSpriteFromAge(sprites);
   }

   public void setTrail(final boolean trail) {
      this.trail = trail;
   }

   public void setTwinkle(final boolean twinkle) {
      this.twinkle = twinkle;
   }

   @Override
   public void extract(final QuadParticleRenderState particleTypeRenderState, final Camera camera, final float partialTickTime) {
      if (!this.twinkle || this.age < this.lifetime / 3 || (this.age + this.lifetime) / 3 % 2 == 0) {
         super.extract(particleTypeRenderState, camera, partialTickTime);
      }
   }

   @Override
   public void tick() {
      super.tick();
      if (this.trail && this.age < this.lifetime / 2 && (this.age + this.lifetime) % 2 == 0) {
         FireworkParticles$SparkParticle sparkParticle = new FireworkParticles$SparkParticle(
            this.level, this.x, this.y, this.z, 0.0, 0.0, 0.0, this.engine, this.sprites
         );
         sparkParticle.setAlpha(0.99F);
         sparkParticle.setColor(this.rCol, this.gCol, this.bCol);
         sparkParticle.age = sparkParticle.lifetime / 2;
         if (this.hasFade) {
            sparkParticle.hasFade = true;
            sparkParticle.fadeR = this.fadeR;
            sparkParticle.fadeG = this.fadeG;
            sparkParticle.fadeB = this.fadeB;
         }

         sparkParticle.twinkle = this.twinkle;
         this.engine.add(sparkParticle);
      }
   }
}
