package com.li64.tide.registries.entities.fish;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.fish.AbstractSchoolingFish;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class Pentapus extends TideVoidFish {
    public Pentapus(EntityType<? extends AbstractSchoolingFish> entityType, Level level) {
        super(entityType, level);
    }

    public boolean hasMovementVector() {
        return this.getDeltaMovement().multiply(1.0, 0.0, 1.0).lengthSqr() > 1.0E-5;
    }

    @Override
    public void travel(@NotNull Vec3 movement) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.0085, 0.0));
        }
        else super.travel(movement);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PentapusRandomMovementGoal(this));
    }

    static class PentapusRandomMovementGoal extends Goal {
        private final Pentapus mob;

        public PentapusRandomMovementGoal(Pentapus mob) {
            this.mob = mob;
        }

        public boolean canUse() {
            return true;
        }

        public void tick() {
            if (!mob.isInWater()) return;
            int i = this.mob.getNoActionTime();
            if (i > 100) {
                this.mob.setDeltaMovement(Vec3.ZERO);
            } else if ((this.mob.getRandom().nextInt(reducedTickDelay(25)) == 0 || !this.mob.wasTouchingWater) && !this.mob.hasMovementVector()) {
                float f = this.mob.getRandom().nextFloat() * ((float)Math.PI * 2F);
                this.mob.setDeltaMovement(new Vec3(
                        Mth.cos(f) * 0.12f,
                        this.mob.getRandom().nextFloat() * 0.26f,
                        Mth.sin(f) * 0.12f)
                );
            }
        }
    }
}
