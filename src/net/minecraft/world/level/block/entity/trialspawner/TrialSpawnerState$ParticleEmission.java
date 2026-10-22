package net.minecraft.world.level.block.entity.trialspawner;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

interface TrialSpawnerState$ParticleEmission {
   TrialSpawnerState$ParticleEmission NONE = (level, random, pos, isOminous) -> {};
   TrialSpawnerState$ParticleEmission SMALL_FLAMES = (level, random, pos, isOminous) -> {
      if (random.nextInt(2) == 0) {
         Vec3 vec = Vec3.atCenterOf(pos).offsetRandom(random, 0.9F);
         addParticle(isOminous ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.SMALL_FLAME, vec, level);
      }
   };
   TrialSpawnerState$ParticleEmission FLAMES_AND_SMOKE = (level, random, pos, isOminous) -> {
      Vec3 vec = Vec3.atCenterOf(pos).offsetRandom(random, 1.0F);
      addParticle(ParticleTypes.SMOKE, vec, level);
      addParticle(isOminous ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME, vec, level);
   };
   TrialSpawnerState$ParticleEmission SMOKE_INSIDE_AND_TOP_FACE = (level, random, pos, isOminous) -> {
      Vec3 vec = Vec3.atCenterOf(pos).offsetRandom(random, 0.9F);
      if (random.nextInt(3) == 0) {
         addParticle(ParticleTypes.SMOKE, vec, level);
      }

      if (level.getGameTime() % 20L == 0L) {
         Vec3 topFaceVec = Vec3.atCenterOf(pos).add(0.0, 0.5, 0.0);
         int smokeCount = level.getRandom().nextInt(4) + 20;

         for (int i = 0; i < smokeCount; i++) {
            addParticle(ParticleTypes.SMOKE, topFaceVec, level);
         }
      }
   };

   private static void addParticle(final SimpleParticleType smoke, final Vec3 vec, final Level level) {
      level.addParticle(smoke, vec.x(), vec.y(), vec.z(), 0.0, 0.0, 0.0);
   }

   void emit(final Level level, final RandomSource random, final BlockPos blockPos, final boolean isOminous);
}
