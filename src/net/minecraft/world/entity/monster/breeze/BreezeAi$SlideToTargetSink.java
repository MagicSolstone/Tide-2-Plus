package net.minecraft.world.entity.monster.breeze;

import com.google.common.annotations.VisibleForTesting;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public class BreezeAi$SlideToTargetSink extends MoveToTargetSink {
   @VisibleForTesting
   public BreezeAi$SlideToTargetSink(final int minTimeout, final int maxTimeout) {
      super(minTimeout, maxTimeout);
   }

   @Override
   protected void start(final ServerLevel level, final Mob body, final long timestamp) {
      super.start(level, body, timestamp);
      body.playSound(SoundEvents.BREEZE_SLIDE);
      body.setPose(Pose.SLIDING);
   }

   @Override
   protected void stop(final ServerLevel level, final Mob body, final long timestamp) {
      super.stop(level, body, timestamp);
      body.setPose(Pose.STANDING);
      if (body.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) {
         body.getBrain().setMemoryWithExpiry(MemoryModuleType.BREEZE_SHOOT, Unit.INSTANCE, 60L);
      }
   }
}
