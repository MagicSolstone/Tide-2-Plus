package net.minecraft.client.particle;

import it.unimi.dsi.fastutil.ints.IntList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.FireworkExplosion$Shape;

@Environment(EnvType.CLIENT)
public class FireworkParticles$Starter extends NoRenderParticle {
   private static final double[][] CREEPER_PARTICLE_COORDS = new double[][]{
      {0.0, 0.2}, {0.2, 0.2}, {0.2, 0.6}, {0.6, 0.6}, {0.6, 0.2}, {0.2, 0.2}, {0.2, 0.0}, {0.4, 0.0}, {0.4, -0.6}, {0.2, -0.6}, {0.2, -0.4}, {0.0, -0.4}
   };
   private static final double[][] STAR_PARTICLE_COORDS = new double[][]{
      {0.0, 1.0},
      {0.3455, 0.309},
      {0.9511, 0.309},
      {0.3795918367346939, -0.12653061224489795},
      {0.6122448979591837, -0.8040816326530612},
      {0.0, -0.35918367346938773}
   };
   private final boolean playSound;
   private int life;
   private final ParticleEngine engine;
   private final List<FireworkExplosion> explosions;
   private boolean twinkleDelay;

   public FireworkParticles$Starter(
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final double xd,
      final double yd,
      final double zd,
      final ParticleEngine engine,
      final List<FireworkExplosion> explosions,
      final boolean playSound
   ) {
      super(level, x, y, z);
      this.xd = xd;
      this.yd = yd;
      this.zd = zd;
      this.engine = engine;
      this.playSound = playSound;
      if (explosions.isEmpty()) {
         throw new IllegalArgumentException("Cannot create firework starter with no explosions");
      } else {
         this.explosions = explosions;
         this.lifetime = explosions.size() * 2 - 1;

         for (FireworkExplosion explosion : explosions) {
            if (explosion.hasTwinkle()) {
               this.twinkleDelay = true;
               this.lifetime += 15;
               break;
            }
         }
      }
   }

   @Override
   public void tick() {
      if (this.life == 0 && this.playSound) {
         boolean farEffect = this.isFarAwayFromCamera();
         boolean largeExplosion = false;
         if (this.explosions.size() >= 3) {
            largeExplosion = true;
         } else {
            for (FireworkExplosion explosion : this.explosions) {
               if (explosion.shape() == FireworkExplosion$Shape.LARGE_BALL) {
                  largeExplosion = true;
                  break;
               }
            }
         }

         SoundEvent sound;
         if (largeExplosion) {
            sound = farEffect ? SoundEvents.FIREWORK_ROCKET_LARGE_BLAST_FAR : SoundEvents.FIREWORK_ROCKET_LARGE_BLAST;
         } else {
            sound = farEffect ? SoundEvents.FIREWORK_ROCKET_BLAST_FAR : SoundEvents.FIREWORK_ROCKET_BLAST;
         }

         this.level.playLocalSound(this.x, this.y, this.z, sound, SoundSource.AMBIENT, 20.0F, 0.95F + this.random.nextFloat() * 0.1F, true);
      }

      if (this.life % 2 == 0 && this.life / 2 < this.explosions.size()) {
         int eIndex = this.life / 2;
         FireworkExplosion explosionx = this.explosions.get(eIndex);
         boolean trail = explosionx.hasTrail();
         boolean twinkle = explosionx.hasTwinkle();
         IntList colors = explosionx.colors();
         IntList fadeColors = explosionx.fadeColors();
         if (colors.isEmpty()) {
            colors = IntList.of(DyeColor.BLACK.getFireworkColor());
         }

         switch (explosionx.shape()) {
            case SMALL_BALL:
               this.createParticleBall(0.25, 2, colors, fadeColors, trail, twinkle);
               break;
            case LARGE_BALL:
               this.createParticleBall(0.5, 4, colors, fadeColors, trail, twinkle);
               break;
            case STAR:
               this.createParticleShape(0.5, STAR_PARTICLE_COORDS, colors, fadeColors, trail, twinkle, false);
               break;
            case CREEPER:
               this.createParticleShape(0.5, CREEPER_PARTICLE_COORDS, colors, fadeColors, trail, twinkle, true);
               break;
            case BURST:
               this.createParticleBurst(colors, fadeColors, trail, twinkle);
         }

         int color = colors.getInt(0);
         this.engine.createParticle(ColorParticleOption.create(ParticleTypes.FLASH, color), this.x, this.y, this.z, 0.0, 0.0, 0.0);
      }

      this.life++;
      if (this.life > this.lifetime) {
         if (this.twinkleDelay && this.playSound) {
            boolean farEffectx = this.isFarAwayFromCamera();
            SoundEvent sound = farEffectx ? SoundEvents.FIREWORK_ROCKET_TWINKLE_FAR : SoundEvents.FIREWORK_ROCKET_TWINKLE;
            this.level.playLocalSound(this.x, this.y, this.z, sound, SoundSource.AMBIENT, 20.0F, 0.9F + this.random.nextFloat() * 0.15F, true);
         }

         this.remove();
      }
   }

   private boolean isFarAwayFromCamera() {
      Minecraft instance = Minecraft.getInstance();
      return instance.gameRenderer.mainCamera().position().distanceToSqr(this.x, this.y, this.z) >= 256.0;
   }

   private void createParticle(
      final double x,
      final double y,
      final double z,
      final double xa,
      final double ya,
      final double za,
      final IntList rgbColors,
      final IntList fadeColors,
      final boolean trail,
      final boolean twinkle
   ) {
      FireworkParticles$SparkParticle sparkParticle = (FireworkParticles$SparkParticle)this.engine.createParticle(ParticleTypes.FIREWORK, x, y, z, xa, ya, za);
      sparkParticle.setTrail(trail);
      sparkParticle.setTwinkle(twinkle);
      sparkParticle.setAlpha(0.99F);
      sparkParticle.setColor(Util.<Integer>getRandom(rgbColors, this.random));
      if (!fadeColors.isEmpty()) {
         sparkParticle.setFadeColor(Util.<Integer>getRandom(fadeColors, this.random));
      }
   }

   private void createParticleBall(
      final double baseSpeed, final int steps, final IntList rgbColors, final IntList fadeColors, final boolean trail, final boolean twinkle
   ) {
      double xx = this.x;
      double yy = this.y;
      double zz = this.z;

      for (int yStep = -steps; yStep <= steps; yStep++) {
         for (int xStep = -steps; xStep <= steps; xStep++) {
            for (int zStep = -steps; zStep <= steps; zStep++) {
               double xa = xStep + (this.random.nextDouble() - this.random.nextDouble()) * 0.5;
               double ya = yStep + (this.random.nextDouble() - this.random.nextDouble()) * 0.5;
               double za = zStep + (this.random.nextDouble() - this.random.nextDouble()) * 0.5;
               double len = Math.sqrt(xa * xa + ya * ya + za * za) / baseSpeed + this.random.nextGaussian() * 0.05;
               this.createParticle(xx, yy, zz, xa / len, ya / len, za / len, rgbColors, fadeColors, trail, twinkle);
               if (yStep != -steps && yStep != steps && xStep != -steps && xStep != steps) {
                  zStep += steps * 2 - 1;
               }
            }
         }
      }
   }

   private void createParticleShape(
      final double baseSpeed,
      final double[][] coords,
      final IntList rgbColors,
      final IntList fadeColors,
      final boolean trail,
      final boolean twinkle,
      final boolean flat
   ) {
      double sx = coords[0][0];
      double sy = coords[0][1];
      this.createParticle(this.x, this.y, this.z, sx * baseSpeed, sy * baseSpeed, 0.0, rgbColors, fadeColors, trail, twinkle);
      float baseAngle = this.random.nextFloat() * (float) Math.PI;
      double angleMod = flat ? 0.034 : 0.34;

      for (int angleStep = 0; angleStep < 3; angleStep++) {
         double angle = baseAngle + angleStep * (float) Math.PI * angleMod;
         double ox = sx;
         double oy = sy;

         for (int c = 1; c < coords.length; c++) {
            double tx = coords[c][0];
            double ty = coords[c][1];

            for (double subStep = 0.25; subStep <= 1.0; subStep += 0.25) {
               double xa = Mth.lerp(subStep, ox, tx) * baseSpeed;
               double ya = Mth.lerp(subStep, oy, ty) * baseSpeed;
               double za = xa * Math.sin(angle);
               xa *= Math.cos(angle);

               for (double flip = -1.0; flip <= 1.0; flip += 2.0) {
                  this.createParticle(this.x, this.y, this.z, xa * flip, ya, za * flip, rgbColors, fadeColors, trail, twinkle);
               }
            }

            ox = tx;
            oy = ty;
         }
      }
   }

   private void createParticleBurst(final IntList rgbColors, final IntList fadeColors, final boolean trail, final boolean twinkle) {
      double baseOffX = this.random.nextGaussian() * 0.05;
      double baseOffZ = this.random.nextGaussian() * 0.05;

      for (int i = 0; i < 70; i++) {
         double xa = this.xd * 0.5 + this.random.nextGaussian() * 0.15 + baseOffX;
         double za = this.zd * 0.5 + this.random.nextGaussian() * 0.15 + baseOffZ;
         double ya = this.yd * 0.5 + this.random.nextDouble() * 0.5;
         this.createParticle(this.x, this.y, this.z, xa, ya, za, rgbColors, fadeColors, trail, twinkle);
      }
   }
}
