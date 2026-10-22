package net.minecraft.world.entity.npc.wanderingtrader;

import java.util.EnumSet;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal$Flag;
import net.minecraft.world.phys.Vec3;

class WanderingTrader$WanderToPositionGoal extends Goal {
   private final WanderingTrader trader;
   private final double stopDistance;
   private final double speedModifier;

   public WanderingTrader$WanderToPositionGoal(
      final WanderingTrader this$0, final WanderingTrader trader, final double stopDistance, final double speedModifier
   ) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.trader = trader;
      this.stopDistance = stopDistance;
      this.speedModifier = speedModifier;
      this.setFlags(EnumSet.of(Goal$Flag.MOVE));
   }

   @Override
   public void stop() {
      this.trader.setWanderTarget(null);
      WanderingTrader.access$000(this.this$0).stop();
   }

   @Override
   public boolean canUse() {
      BlockPos wanderPosition = this.trader.getWanderTarget();
      return wanderPosition != null && this.isTooFarAway(wanderPosition, this.stopDistance);
   }

   @Override
   public void tick() {
      BlockPos wanderPosition = this.trader.getWanderTarget();
      if (wanderPosition != null && WanderingTrader.access$100(this.this$0).isDone()) {
         if (this.isTooFarAway(wanderPosition, 10.0)) {
            Vec3 dir = new Vec3(
                  wanderPosition.getX() - this.trader.getX(), wanderPosition.getY() - this.trader.getY(), wanderPosition.getZ() - this.trader.getZ()
               )
               .normalize();
            Vec3 targetPos = dir.scale(10.0).add(this.trader.getX(), this.trader.getY(), this.trader.getZ());
            WanderingTrader.access$200(this.this$0).moveTo(targetPos.x, targetPos.y, targetPos.z, this.speedModifier);
         } else {
            WanderingTrader.access$300(this.this$0).moveTo(wanderPosition.getX(), wanderPosition.getY(), wanderPosition.getZ(), this.speedModifier);
         }
      }
   }

   private boolean isTooFarAway(final BlockPos pos, final double distance) {
      return !pos.closerToCenterThan(this.trader.position(), distance);
   }
}
