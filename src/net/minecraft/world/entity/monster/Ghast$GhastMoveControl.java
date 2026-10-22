package net.minecraft.world.entity.monster;

import java.util.function.BooleanSupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.MoveControl$Operation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class Ghast$GhastMoveControl<T extends Mob> extends MoveControl<T> {
   private int floatDuration;
   private final boolean careful;
   private final BooleanSupplier shouldBeStopped;

   public Ghast$GhastMoveControl(final T ghast, final boolean careful, final BooleanSupplier shouldBeStopped) {
      super(ghast);
      this.careful = careful;
      this.shouldBeStopped = shouldBeStopped;
   }

   @Override
   public void tick() {
      if (this.shouldBeStopped.getAsBoolean()) {
         this.operation = MoveControl$Operation.WAIT;
         this.mob.stopInPlace();
      }

      if (this.operation == MoveControl$Operation.MOVE_TO) {
         if (this.floatDuration-- <= 0) {
            this.floatDuration = this.floatDuration + this.mob.getRandom().nextInt(5) + 2;
            Vec3 travel = new Vec3(this.wantedX - this.mob.getX(), this.wantedY - this.mob.getY(), this.wantedZ - this.mob.getZ());
            if (this.canReach(travel)) {
               this.mob
                  .setDeltaMovement(this.mob.getDeltaMovement().add(travel.normalize().scale(this.mob.getAttributeValue(Attributes.FLYING_SPEED) * 5.0 / 3.0)));
            } else {
               this.operation = MoveControl$Operation.WAIT;
            }
         }
      }
   }

   private boolean canReach(final Vec3 travel) {
      AABB aabb = this.mob.getBoundingBox();
      AABB aabbAtDestination = aabb.move(travel);
      if (this.careful) {
         for (BlockPos pos : BlockPos.betweenClosed(aabbAtDestination.inflate(1.0))) {
            if (!this.blockTraversalPossible(this.mob.level(), null, null, pos, false, false)) {
               return false;
            }
         }
      }

      boolean isInWater = this.mob.isInWater();
      boolean isInLava = this.mob.isInLava();
      Vec3 start = this.mob.position();
      Vec3 end = start.add(travel);
      return BlockGetter.forEachBlockIntersectedBetween(
         start,
         end,
         aabbAtDestination,
         (blockPos, i) -> aabb.intersects(blockPos) ? true : this.blockTraversalPossible(this.mob.level(), start, end, blockPos, isInWater, isInLava)
      );
   }

   private boolean blockTraversalPossible(
      final BlockGetter level,
      @Nullable final Vec3 start,
      @Nullable final Vec3 end,
      final BlockPos pos,
      final boolean canPathThroughWater,
      final boolean canPathThroughLava
   ) {
      BlockState state = level.getBlockState(pos);
      if (state.isAir()) {
         return true;
      } else {
         boolean preciseBlockCollisions = start != null && end != null;
         boolean pathNoCollisions = preciseBlockCollisions
            ? !this.mob.collidedWithShapeMovingFrom(start, end, state.getCollisionShape(level, pos).move(new Vec3(pos)).toAabbs())
            : state.getCollisionShape(level, pos).isEmpty();
         if (!this.careful) {
            return pathNoCollisions;
         } else if (state.is(BlockTags.HAPPY_GHAST_AVOIDS)) {
            return false;
         } else {
            FluidState fluidState = level.getFluidState(pos);
            if (!fluidState.isEmpty() && (!preciseBlockCollisions || this.mob.collidedWithFluid(fluidState, pos, start, end))) {
               if (fluidState.is(FluidTags.WATER)) {
                  return canPathThroughWater;
               }

               if (fluidState.is(FluidTags.LAVA)) {
                  return canPathThroughLava;
               }
            }

            return pathNoCollisions;
         }
      }
   }
}
