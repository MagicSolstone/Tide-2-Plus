package com.li64.tide.registries.entities.fish.special;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class VoidBoundPathNavigation extends PathNavigation {
    private boolean allowBreaching;

    public VoidBoundPathNavigation(Mob mob, Level level) {
        super(mob, level);
    }

    protected @NotNull PathFinder createPathFinder(int p_26598_) {
        this.allowBreaching = this.mob instanceof WaterAnimal;
        this.nodeEvaluator = new VoidSwimNodeEvaluator(this.allowBreaching);
        return new PathFinder(this.nodeEvaluator, p_26598_);
    }

    protected Path createPath(Set<BlockPos> targets, int regionOffset, boolean offsetUpward, int accuracy, float followRange) {
        if (targets.isEmpty()) return null;
        if (!this.canUpdatePath()) return null;
        if (this.path != null && !this.path.isDone() && targets.contains(this.targetPos)) return this.path;
        else {
            
            BlockPos blockpos = offsetUpward ? this.mob.blockPosition().above() : this.mob.blockPosition();
            int i = (int)(followRange + (float)regionOffset);
            PathNavigationRegion pathnavigationregion = new PathNavigationRegion(this.level, blockpos.offset(-i, -i, -i), blockpos.offset(i, i, i));
            Path path = this.pathFinder.findPath(pathnavigationregion, this.mob, targets, followRange, accuracy, this.maxVisitedNodesMultiplier);
            
            if (path != null) {
                this.targetPos = path.getTarget();
                this.reachRange = accuracy;
                this.resetStuckTimeout();
            }

            return path;
        }
    }

    protected boolean canUpdatePath() {
        return this.allowBreaching || this.mob.isInWater() || this.mob.isInLava();
    }

    protected @NotNull Vec3 getTempMobPos() {
        return new Vec3(this.mob.getX(), this.mob.getY(0.5), this.mob.getZ());
    }

    protected double getGroundY(Vec3 vec) {
        return vec.y;
    }

    protected boolean canMoveDirectly(@NotNull Vec3 pos1, @NotNull Vec3 pos2) {
        return isClearForMovementBetween(this.mob, pos1, pos2, false);
    }

    public boolean isStableDestination(@NotNull BlockPos pos) {
        return !this.level.getBlockState(pos).isSolidRender();
    }

    public void setCanFloat(boolean canSwim) {}

    @Override
    public boolean canNavigateGround() {
        return false;
    }
}
