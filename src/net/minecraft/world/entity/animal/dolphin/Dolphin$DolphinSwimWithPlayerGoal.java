package net.minecraft.world.entity.animal.dolphin;

import java.util.EnumSet;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal$Flag;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

class Dolphin$DolphinSwimWithPlayerGoal extends Goal {
   private final Dolphin dolphin;
   private final double speedModifier;
   @Nullable
   private Player player;

   public Dolphin$DolphinSwimWithPlayerGoal(final Dolphin dolphin, final double speedModifier) {
      this.dolphin = dolphin;
      this.speedModifier = speedModifier;
      this.setFlags(EnumSet.of(Goal$Flag.MOVE, Goal$Flag.LOOK));
   }

   @Override
   public boolean canUse() {
      this.player = getServerLevel(this.dolphin).getNearestPlayer(Dolphin.SWIM_WITH_PLAYER_TARGETING, this.dolphin);
      return this.player == null ? false : this.player.isSwimming() && this.dolphin.getTarget() != this.player;
   }

   @Override
   public boolean canContinueToUse() {
      return this.player != null && this.player.isSwimming() && this.dolphin.distanceToSqr(this.player) < 256.0;
   }

   @Override
   public void start() {
      this.player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 100), this.dolphin);
   }

   @Override
   public void stop() {
      this.player = null;
      this.dolphin.getNavigation().stop();
   }

   @Override
   public void tick() {
      this.dolphin.getLookControl().setLookAt(this.player, this.dolphin.getMaxHeadYRot() + 20, this.dolphin.getMaxHeadXRot());
      if (this.dolphin.distanceToSqr(this.player) < 6.25) {
         this.dolphin.getNavigation().stop();
      } else {
         this.dolphin.getNavigation().moveTo(this.player, this.speedModifier);
      }

      if (this.player.isSwimming() && this.player.level().getRandom().nextInt(6) == 0) {
         this.player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 100), this.dolphin);
      }
   }
}
