package com.li64.tide.registries.entities.fish.lava;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.pathfinder.PathType;

public abstract class LavaAnimal extends PathfinderMob {
    protected LavaAnimal(EntityType<? extends LavaAnimal> type, Level level) {
        super(type, level);
        this.setPathfindingMalus(PathType.LAVA, 0f);
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader level) {
        return level.isUnobstructed(this);
    }

    @Override
    public int getAmbientSoundInterval() {
        return 120;
    }

    protected void handleAirSupply(ServerLevel level, int airSupply) {
        if (this.isAlive() && !this.isInLava()) {
            this.setAirSupply(airSupply - 1);
            if (this.getAirSupply() == -20) {
                this.setAirSupply(0);
                this.hurt(this.damageSources().drown(), 2.0F);
            }
        }
        else this.setAirSupply(300);
    }

    @Override
    public void baseTick() {
        int i = this.getAirSupply();
        super.baseTick();
        Level level = this.level();
        if (level instanceof ServerLevel serverlevel) {
            this.handleAirSupply(serverlevel, i);
        }
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    protected int getBaseExperienceReward(net.minecraft.server.level.ServerLevel level) {
        if (isInLava()) return 0;
        return super.getBaseExperienceReward(level);
    }

    @Override
    public boolean canBeLeashed() {
        return
        false;
    }
}
