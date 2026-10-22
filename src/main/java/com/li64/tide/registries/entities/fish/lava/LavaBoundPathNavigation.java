package com.li64.tide.registries.entities.fish.lava;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class LavaBoundPathNavigation extends PathNavigation {
    private boolean allowBreaching;

    public LavaBoundPathNavigation(Mob mob, Level level) {
        super(mob, level);
    }

    protected @NotNull PathFinder createPathFinder(int p_26598_) {
        this.allowBreaching = this.mob instanceof LavaAnimal;
        this.nodeEvaluator = new LavaSwimNodeEvaluator(this.allowBreaching);
        return new PathFinder(this.nodeEvaluator, p_26598_);
    }

    protected boolean canUpdatePath() {
        return this.allowBreaching || this.mob.isInWater() || mob.isInLava();
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
