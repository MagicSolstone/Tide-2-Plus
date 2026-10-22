package net.minecraft.world.entity.animal.squid;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

class Squid$SquidFleeGoal extends Goal {
   private static final float SQUID_FLEE_SPEED = 3.0F;
   private static final float SQUID_FLEE_MIN_DISTANCE = 5.0F;
   private static final float SQUID_FLEE_MAX_DISTANCE = 10.0F;
   private int fleeTicks;

   private Squid$SquidFleeGoal(final Squid this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
   }

   @Override
   public boolean canUse() {
      LivingEntity entity = this.this$0.getLastHurtByMob();
      return this.this$0.isInWater() && entity != null ? this.this$0.distanceToSqr(entity) < 100.0 : false;
   }

   @Override
   public void start() {
      this.fleeTicks = 0;
   }

   @Override
   public boolean requiresUpdateEveryTick() {
      return true;
   }

   @Override
   public void tick() {
      this.fleeTicks++;
      LivingEntity lastHurtByMob = this.this$0.getLastHurtByMob();
      if (lastHurtByMob != null) {
         Vec3 fleeTo = new Vec3(this.this$0.getX() - lastHurtByMob.getX(), this.this$0.getY() - lastHurtByMob.getY(), this.this$0.getZ() - lastHurtByMob.getZ());
         BlockState blockState = this.this$0
            .level()
            .getBlockState(BlockPos.containing(this.this$0.getX() + fleeTo.x, this.this$0.getY() + fleeTo.y, this.this$0.getZ() + fleeTo.z));
         FluidState fluidState = this.this$0
            .level()
            .getFluidState(BlockPos.containing(this.this$0.getX() + fleeTo.x, this.this$0.getY() + fleeTo.y, this.this$0.getZ() + fleeTo.z));
         if (fluidState.is(FluidTags.WATER) || blockState.isAir()) {
            double length = fleeTo.length();
            if (length > 0.0) {
               fleeTo.normalize();
               double avoidSpeed = 3.0;
               if (length > 5.0) {
                  avoidSpeed -= (length - 5.0) / 5.0;
               }

               if (avoidSpeed > 0.0) {
                  fleeTo = fleeTo.scale(avoidSpeed);
               }
            }

            if (blockState.isAir()) {
               fleeTo = fleeTo.subtract(0.0, fleeTo.y, 0.0);
            }

            this.this$0.movementVector = new Vec3(fleeTo.x / 20.0, fleeTo.y / 20.0, fleeTo.z / 20.0);
         }

         if (this.fleeTicks % 10 == 5) {
            this.this$0.level().addParticle(ParticleTypes.BUBBLE, this.this$0.getX(), this.this$0.getY(), this.this$0.getZ(), 0.0, 0.0, 0.0);
         }
      }
   }
}
