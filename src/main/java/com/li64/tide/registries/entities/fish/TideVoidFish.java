package com.li64.tide.registries.entities.fish;

import com.li64.tide.data.fishing.mediums.FishingMedium;
import com.li64.tide.registries.entities.fish.special.VoidBoundPathNavigation;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FollowFlockLeaderGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.entity.animal.fish.AbstractSchoolingFish;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class TideVoidFish extends TideFishEntity {
    public TideVoidFish(EntityType<? extends AbstractSchoolingFish> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new VoidFishMoveControl(this);
    }

    @Override
    public void travel(@NotNull Vec3 movement) {
        if (this.isInVoid()) this.resetFallDistance();
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed() / 50f, movement);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.005, 0.0));
        }
        else super.travel(movement);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(4, new VoidFishSwimGoal(this));
        this.goalSelector.addGoal(5, new FollowFlockLeaderGoal(this));
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new VoidBoundPathNavigation(this, level);
    }

    public boolean isInVoid() {
        return FishingMedium.VOID.isInVoid(position(), level());
    }

    @Override
    public boolean isInWater() {
        return super.isInWater() || this.isInVoid();
    }

    static class VoidFishMoveControl extends MoveControl {
        private final TideVoidFish fish;

        VoidFishMoveControl(TideVoidFish fish) {
            super(fish);
            this.fish = fish;
        }

        public void tick() {
            if (this.fish.isEyeInFluid(FluidTags.WATER) || this.fish.isInVoid()) {
                this.fish.setDeltaMovement(this.fish.getDeltaMovement().add(0.0, 0.005, 0.0));
            }

            if (this.operation == Operation.MOVE_TO && !this.fish.getNavigation().isDone()) {
                float f = (float)(this.speedModifier * this.fish.getAttributeValue(Attributes.MOVEMENT_SPEED));
                this.fish.setSpeed(Mth.lerp(0.125f, this.fish.getSpeed(), f));
                double d0 = this.wantedX - this.fish.getX();
                double d1 = this.wantedY - this.fish.getY();
                double d2 = this.wantedZ - this.fish.getZ();
                if (d1 != 0.0) {
                    double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                    this.fish.setDeltaMovement(this.fish.getDeltaMovement().add(0.0, (double)this.fish.getSpeed() * (d1 / d3) * 0.1, 0.0));
                }

                if (d0 != 0.0 || d2 != 0.0) {
                    float f1 = (float)(Mth.atan2(d2, d0) * 180.0 / Math.PI) - 90.0f;
                    this.fish.setYRot(this.rotlerp(this.fish.getYRot(), f1, 90.0f));
                    this.fish.yBodyRot = this.fish.getYRot();
                }
            }
            else this.fish.setSpeed(0.0f);
        }
    }

    static class VoidFishSwimGoal extends RandomStrollGoal {
        public VoidFishSwimGoal(TideVoidFish fish) {
            this(fish, 1.0, 40);
        }

        public VoidFishSwimGoal(TideVoidFish fish, double speedModifier, int interval) {
            super(fish, speedModifier, interval);
        }

        @Override
        public boolean canUse() {
            if (!this.mob.isInWater() || this.mob.isPassenger() || mob.getTarget() != null) return false;
            if (!this.forceTrigger && this.mob.getRandom().nextInt(this.interval) >= 100) return false;
            if (!((TideVoidFish)mob).canRandomSwim()) return false;

            Vec3 pos = this.getPosition();
            if (pos == null) return false;

            this.wantedX = pos.x;
            this.wantedY = pos.y;
            this.wantedZ = pos.z;

            this.forceTrigger = false;
            return true;
        }

        @Override
        protected Vec3 getPosition() {
            Vec3 pos = getRandomPos(mob, 10, 7);
            for (int i = 0; pos != null && !isValidPos(pos) && i < 9; i++)
                pos = getRandomPos(mob, 10, 7);
            return pos;
        }

        private boolean isValidPos(Vec3 pos) {
            return (this.mob.level().isEmptyBlock(BlockPos.containing(pos)) && FishingMedium.VOID.isInVoid(pos, mob.level()))
                    || this.mob.level().getFluidState(BlockPos.containing(pos)).is(FluidTags.WATER);
        }

        protected Vec3 getRandomPos(PathfinderMob mob, int radius, int verticalDistance) {
            boolean flag = GoalUtils.mobRestricted(mob, radius);
            return RandomPos.generateRandomPos(mob, () -> {
                BlockPos direction = RandomPos.generateRandomDirection(mob.getRandom(), radius, verticalDistance);
                return generateRandomPosTowardDirection(mob, radius, flag, direction);
            });
        }

        private static BlockPos generateRandomPosTowardDirection(PathfinderMob mob, int radius, boolean shortCircuit, BlockPos direction) {
            BlockPos newPos = RandomPos.generateRandomPosTowardDirection(mob, radius, mob.getRandom(), direction);
            return !GoalUtils.isRestricted(shortCircuit, mob, newPos) ? newPos : null;
        }
    }
}
