package net.minecraft.world.entity;

import java.util.LinkedList;
import java.util.List;
import net.minecraft.core.PositionAndRotation;
import net.minecraft.core.PositionAndRotation$Mutable;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

class SteppedInterpolationHandler$InterpolationData extends PositionAndRotation$Mutable {
   private final LinkedList<SteppedInterpolationHandler$Step> remainingSteps = new LinkedList<>();
   private final PositionAndRotation$Mutable lastStepPosRot = new PositionAndRotation$Mutable();
   private float currentStepTicks;
   private float remainingTicks;
   private float interpolationSpeed = 1.0F;

   private SteppedInterpolationHandler$InterpolationData() {
   }

   private void advance(float ticks, final int interpolationSteps) {
      float targetSpeed = Math.max(this.remainingTicks / interpolationSteps, 1.0F);
      this.interpolationSpeed = Mth.lerp(1.0F / interpolationSteps, this.interpolationSpeed, targetSpeed);
      if (ticks * this.interpolationSpeed < this.remainingTicks) {
         ticks *= this.interpolationSpeed;
      } else {
         ticks = this.remainingTicks;
         this.interpolationSpeed = 1.0F;
      }

      this.currentStepTicks += ticks;
      this.remainingTicks -= ticks;
   }

   private void reset() {
      this.remainingSteps.clear();
      this.remainingTicks = 0.0F;
      this.interpolationSpeed = 1.0F;
   }

   @Override
   public void addDelta(final Vec3 delta) {
      super.addDelta(delta);

      for (SteppedInterpolationHandler$Step step : this.remainingSteps) {
         step.addDelta(delta);
      }

      this.lastStepPosRot.addDelta(delta);
   }

   @Override
   public void addRotation(final float yRot, final float xRot) {
      super.addRotation(yRot, xRot);

      for (SteppedInterpolationHandler$Step step : this.remainingSteps) {
         step.addRotation(yRot, xRot);
      }

      this.lastStepPosRot.addRotation(yRot, xRot);
   }

   private void setStartingPoint(final Vec3 position, final float yRot, final float xRot) {
      this.lastStepPosRot.set(position, yRot, xRot);
      this.currentStepTicks = 1.0F;
   }

   private void addStep(final Vec3 position, final float yRot, final float xRot, final int interpolationSteps) {
      this.remainingSteps.add(new SteppedInterpolationHandler$Step(position, yRot, xRot, interpolationSteps));
      this.remainingTicks += interpolationSteps;
   }

   // $VF: One or more variable merging failures!
   // $VF: Could not properly define all variable types!
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private void addSteps(final PositionPath position, final float yRot, final float xRot, final int interpolationSteps) {
      switch (position) {
         case PositionPath$Linear(Vec3 var19):
            this.addStep(var19, yRot, xRot, interpolationSteps);
            break;
         case PositionPath$Stepped var9: {
            PositionPath$Stepped var10000 = var9;

            try {
               var10000.endPosition();
            } catch (Throwable var17) {
               throw new MatchException(var17.toString(), var17);
            }

            var10000 = var9;

            try {
               var24 = var10000.steps();
            } catch (Throwable var16) {
               throw new MatchException(var16.toString(), var16);
            }

            List totalInterpolationTicks = var24;
            if (yRot == this.yRot() && xRot == this.xRot()) {
               for (PositionStep step : totalInterpolationTicks) {
                  this.addStep(step.position(), yRot, xRot, step.tickOffset());
               }

               return;
            }

            int totalInterpolationTicks = getInterpolationTicks(totalInterpolationTicks);
            int offset = 0;

            <unknown> var10;
            for (PositionStep step : var10_1) {
               offset += step.tickOffset();
               float a = (float)offset / totalInterpolationTicks;
               this.addStep(step.position(), Mth.rotLerp(a, this.yRot(), yRot), Mth.lerp(a, this.xRot(), xRot), step.tickOffset());
            }
            break;
         }
         default:
            throw new MatchException(null, null);
      }
   }

   private static int getInterpolationTicks(final List<PositionStep> steps) {
      int ticks = 0;

      for (PositionStep step : steps) {
         ticks += step.tickOffset();
      }

      return ticks;
   }

   private PositionAndRotation getNewPositionAndRotation() {
      while (!this.remainingSteps.isEmpty()) {
         SteppedInterpolationHandler$Step step = this.remainingSteps.getFirst();
         int offset = step.tickOffset;
         if (this.currentStepTicks < offset) {
            float a = this.currentStepTicks / offset;
            return PositionAndRotation.of(
               this.lastStepPosRot.position().lerp(step.position(), a),
               Mth.rotLerp(a, this.lastStepPosRot.yRot(), step.yRot()),
               Mth.lerp(a, this.lastStepPosRot.xRot(), step.xRot())
            );
         }

         this.currentStepTicks -= offset;
         this.lastStepPosRot.set(step.position(), step.yRot(), step.xRot());
         this.remainingSteps.removeFirst();
      }

      return this;
   }
}
