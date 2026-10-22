package net.minecraft.world.entity.animal.armadillo;

import java.util.Map;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.TimeUtil;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public class ArmadilloAi$ArmadilloBallUp extends Behavior<Armadillo> {
   private static final int BALL_UP_STAY_IN_STATE = 5 * TimeUtil.SECONDS_PER_MINUTE * 20;
   private static final int TICKS_DELAY_TO_DETERMINE_IF_DANGER_IS_STILL_AROUND = 5;
   private static final int DANGER_DETECTED_RECENTLY_DANGER_THRESHOLD = 75;
   private int nextPeekTimer = 0;
   private boolean dangerWasAround;

   public ArmadilloAi$ArmadilloBallUp() {
      super(Map.of(), BALL_UP_STAY_IN_STATE);
   }

   protected void tick(final ServerLevel level, final Armadillo body, final long timestamp) {
      super.tick(level, body, timestamp);
      if (this.nextPeekTimer > 0) {
         this.nextPeekTimer--;
      }

      if (body.shouldSwitchToScaredState()) {
         body.switchToState(Armadillo$ArmadilloState.SCARED);
         if (body.onGround()) {
            body.playSound(SoundEvents.ARMADILLO_LAND);
         }
      } else {
         Armadillo$ArmadilloState state = body.getState();
         long dangerTickCounter = body.getBrain().getTimeUntilExpiry(MemoryModuleType.DANGER_DETECTED_RECENTLY);
         boolean dangerIsAround = dangerTickCounter > 75L;
         if (dangerIsAround != this.dangerWasAround) {
            this.nextPeekTimer = this.pickNextPeekTimer(body);
         }

         this.dangerWasAround = dangerIsAround;
         if (state == Armadillo$ArmadilloState.SCARED) {
            if (this.nextPeekTimer == 0 && body.onGround() && dangerIsAround) {
               level.broadcastEntityEvent(body, (byte)64);
               this.nextPeekTimer = this.pickNextPeekTimer(body);
            }

            if (dangerTickCounter < Armadillo$ArmadilloState.UNROLLING.animationDuration()) {
               body.playSound(SoundEvents.ARMADILLO_UNROLL_START);
               body.switchToState(Armadillo$ArmadilloState.UNROLLING);
            }
         } else if (state == Armadillo$ArmadilloState.UNROLLING && dangerTickCounter > Armadillo$ArmadilloState.UNROLLING.animationDuration()) {
            body.switchToState(Armadillo$ArmadilloState.SCARED);
         }
      }
   }

   private int pickNextPeekTimer(final Armadillo body) {
      return Armadillo$ArmadilloState.SCARED.animationDuration() + body.getRandom().nextIntBetweenInclusive(100, 400);
   }

   protected boolean checkExtraStartConditions(final ServerLevel level, final Armadillo body) {
      return body.onGround() && !body.isInLiquid();
   }

   protected boolean canStillUse(final ServerLevel level, final Armadillo body, final long timestamp) {
      return body.getState().isThreatened();
   }

   protected void start(final ServerLevel level, final Armadillo body, final long timestamp) {
      body.rollUp();
   }

   protected void stop(final ServerLevel level, final Armadillo body, final long timestamp) {
      if (!body.canStayRolledUp()) {
         body.rollOut();
      }
   }
}
