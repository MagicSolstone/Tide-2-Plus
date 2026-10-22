package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.material.Fluid;

public class FloatGoal extends Goal {
   private final Mob mob;
   protected final TagKey<Fluid> fluid;

   public FloatGoal(final Mob mob) {
      this(mob, FluidTags.ENTITY_FLOATABLE);
   }

   public FloatGoal(final Mob mob, final TagKey<Fluid> fluid) {
      this.mob = mob;
      this.fluid = fluid;
      this.setFlags(EnumSet.of(Goal$Flag.JUMP));
      mob.getNavigation().setCanFloat(true);
   }

   @Override
   public boolean canUse() {
      return this.mob.isInFluidDeeperThan(this.mob.getFluidJumpThreshold(), this.fluid) || this.mob.isInLava();
   }

   @Override
   public boolean requiresUpdateEveryTick() {
      return true;
   }

   @Override
   public void tick() {
      if (this.mob.getRandom().nextFloat() < 0.8F) {
         this.mob.getJumpControl().jump();
      }
   }
}
