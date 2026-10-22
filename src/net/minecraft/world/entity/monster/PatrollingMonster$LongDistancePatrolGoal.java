package net.minecraft.world.entity.monster;

import java.util.EnumSet;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal$Flag;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.levelgen.Heightmap$Types;
import net.minecraft.world.phys.Vec3;

public class PatrollingMonster$LongDistancePatrolGoal<T extends PatrollingMonster> extends Goal {
   private static final int NAVIGATION_FAILED_COOLDOWN = 200;
   private final T mob;
   private final double speedModifier;
   private final double leaderSpeedModifier;
   private long cooldownUntil;

   public PatrollingMonster$LongDistancePatrolGoal(final T mob, final double speedModifier, final double leaderSpeedModifier) {
      this.mob = mob;
      this.speedModifier = speedModifier;
      this.leaderSpeedModifier = leaderSpeedModifier;
      this.cooldownUntil = -1L;
      this.setFlags(EnumSet.of(Goal$Flag.MOVE));
   }

   @Override
   public boolean canUse() {
      boolean isOnCooldown = this.mob.level().getGameTime() < this.cooldownUntil;
      return this.mob.isPatrolling() && this.mob.getTarget() == null && !this.mob.hasControllingPassenger() && this.mob.hasPatrolTarget() && !isOnCooldown;
   }

   @Override
   public void start() {
   }

   @Override
   public void stop() {
   }

   @Override
   public void tick() {
      boolean patrolLeader = this.mob.isPatrolLeader();
      PathNavigation navigation = this.mob.getNavigation();
      if (navigation.isDone()) {
         List<PatrollingMonster> companions = this.findPatrolCompanions();
         if (this.mob.isPatrolling() && companions.isEmpty()) {
            this.mob.setPatrolling(false);
         } else if (patrolLeader && this.mob.getPatrolTarget().closerToCenterThan(this.mob.position(), 10.0)) {
            this.mob.findPatrolTarget();
         } else {
            Vec3 longDistanceTarget = Vec3.atBottomCenterOf(this.mob.getPatrolTarget());
            Vec3 selfVector = this.mob.position();
            Vec3 distance = selfVector.subtract(longDistanceTarget);
            longDistanceTarget = distance.yRot(90.0F).scale(0.4).add(longDistanceTarget);
            Vec3 moveTarget = longDistanceTarget.subtract(selfVector).normalize().scale(10.0).add(selfVector);
            BlockPos pathTarget = BlockPos.containing(moveTarget);
            pathTarget = this.mob.level().getHeightmapPos(Heightmap$Types.MOTION_BLOCKING_NO_LEAVES, pathTarget);
            if (!navigation.moveTo(pathTarget.getX(), pathTarget.getY(), pathTarget.getZ(), patrolLeader ? this.leaderSpeedModifier : this.speedModifier)) {
               this.moveRandomly();
               this.cooldownUntil = this.mob.level().getGameTime() + 200L;
            } else if (patrolLeader) {
               for (PatrollingMonster companion : companions) {
                  companion.setPatrolTarget(pathTarget);
               }
            }
         }
      }
   }

   private List<PatrollingMonster> findPatrolCompanions() {
      return this.mob
         .level()
         .getEntitiesOfClass(PatrollingMonster.class, this.mob.getBoundingBox().inflate(16.0), mob -> mob.canJoinPatrol() && !mob.is(this.mob));
   }

   private boolean moveRandomly() {
      RandomSource random = this.mob.getRandom();
      BlockPos pathTarget = this.mob
         .level()
         .getHeightmapPos(Heightmap$Types.MOTION_BLOCKING_NO_LEAVES, this.mob.blockPosition().offset(-8 + random.nextInt(16), 0, -8 + random.nextInt(16)));
      return this.mob.getNavigation().moveTo(pathTarget.getX(), pathTarget.getY(), pathTarget.getZ(), this.speedModifier);
   }
}
