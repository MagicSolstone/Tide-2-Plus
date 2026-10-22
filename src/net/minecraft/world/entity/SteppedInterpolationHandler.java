package net.minecraft.world.entity;

import java.util.Objects;
import net.minecraft.core.PositionAndRotation;
import net.minecraft.core.PositionAndRotation$Mutable;
import net.minecraft.world.phys.Vec3;

public class SteppedInterpolationHandler extends AbstractInterpolationHandler {
   private final SteppedInterpolationHandler$InterpolationData interpolationData = new SteppedInterpolationHandler$InterpolationData();

   private SteppedInterpolationHandler(final Entity entity) {
      super(entity, entity.getType().updateInterval());
   }

   public static InterpolationHandler create(final Entity entity) {
      return (InterpolationHandler)(entity.level().isClientSide() ? new SteppedInterpolationHandler(entity) : new SteppedInterpolationTracker(entity));
   }

   @Override
   protected PositionAndRotation$Mutable interpolationData() {
      return this.interpolationData;
   }

   @Override
   protected void startInterpolating(final PositionPath position, final float yRot, final float xRot) {
      if (!this.hasActiveInterpolation()) {
         this.interpolationData.setStartingPoint(this.entity.position(), this.entity.getYRot(), this.entity.getXRot());
      }

      Vec3 endPosition = position.endPosition();
      if (Objects.equals(endPosition, this.interpolationData.position())) {
         this.interpolationData.addStep(endPosition, yRot, xRot, this.interpolationSteps);
      } else {
         this.interpolationData.addSteps(position, yRot, xRot, this.interpolationSteps);
      }

      this.interpolationData.set(endPosition, yRot, xRot);
   }

   @Override
   protected void doInterpolate() {
      PositionAndRotation target = this.interpolationData.getNewPositionAndRotation();
      this.entity.setPos(target.position());
      this.entity.setRot(target.yRot(), target.xRot());
      float tick = this.entity.level().getRelativeTickSpeed();
      this.interpolationData.advance(tick, this.interpolationSteps);
   }

   @Override
   public boolean hasActiveInterpolation() {
      return !this.interpolationData.remainingSteps.isEmpty();
   }

   @Override
   public void cancel() {
      this.interpolationData.reset();
   }
}
