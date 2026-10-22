package com.li64.tide.registries.entities.fish.lava;

import com.mojang.datafixers.DataFixUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class TideSchoolingLavaFish extends TideLavaFish {
    private TideSchoolingLavaFish leader;
    private int schoolSize = 1;

    public TideSchoolingLavaFish(EntityType<? extends TideSchoolingLavaFish> entityType, Level level) {
        super(entityType, level);
    }
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(5, new FollowFlockLeaderGoal(this));
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return this.getMaxSchoolSize();
    }

    public int getMaxSchoolSize() {
        return super.getMaxSpawnClusterSize();
    }

    @Override
    protected boolean canRandomSwim() {
        return !this.isFollower();
    }

    public boolean isFollower() {
        return this.leader != null && this.leader.isAlive();
    }

    public boolean hasFollowers() {
        return this.schoolSize > 1;
    }

    public void startFollowing(TideSchoolingLavaFish leader) {
        if (hasFollowers() || leader.getType() != getType()) return;
        this.leader = leader;
        leader.addFollower();
    }

    public void stopFollowing() {
        this.leader.removeFollower();
        this.leader = null;
    }

    private void addFollower() {
        this.schoolSize++;
    }

    private void removeFollower() {
        this.schoolSize--;
    }

    public boolean canBeFollowed() {
        return this.hasFollowers() && this.schoolSize < this.getMaxSchoolSize();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.hasFollowers() && this.level().getRandom().nextInt(200) == 1) {
            List<? extends TideLavaFish> list = this.level().getEntitiesOfClass(
                    (Class<? extends TideLavaFish>)this.getClass(),
                    this.getBoundingBox().inflate(8.0, 8.0, 8.0)
            );
            if (list.size() <= 1) this.schoolSize = 1;
        }
    }

    public boolean inRangeOfLeader() {
        return this.distanceToSqr(this.leader) <= 121.0;
    }

    public void pathToLeader() {
        if (this.isFollower()) {
            this.getNavigation().moveTo(this.leader, 1.0);
        }
    }

    public void addFollowers(Stream<? extends TideSchoolingLavaFish> followers) {
        followers.limit(this.getMaxSchoolSize() - this.schoolSize)
                .filter(f -> f != this)
                .forEach(f -> f.startFollowing(this));
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnType, @Nullable SpawnGroupData groupData) {
        super.finalizeSpawn(level, difficulty, spawnType, groupData);
        if (groupData == null) groupData = new SchoolSpawnGroupData(this);
        else this.startFollowing(((SchoolSpawnGroupData) groupData).leader);
        return groupData;
    }

    public record SchoolSpawnGroupData(TideSchoolingLavaFish leader) implements SpawnGroupData {}

    static class FollowFlockLeaderGoal extends Goal {
        private final TideSchoolingLavaFish mob;
        private int timeToRecalcPath;
        private int nextStartTick;

        public FollowFlockLeaderGoal(TideSchoolingLavaFish fish) {
            this.mob = fish;
            this.nextStartTick = this.nextStartTick(fish);
        }

        protected int nextStartTick(TideSchoolingLavaFish taskOwner) {
            return reducedTickDelay(200 + taskOwner.getRandom().nextInt(200) % 20);
        }

        public boolean canUse() {
            if (this.mob.hasFollowers()) return false;
            if (this.mob.isFollower()) return true;
            if (this.nextStartTick > 0) {
                --this.nextStartTick;
                return false;
            }
            this.nextStartTick = this.nextStartTick(this.mob);
            Predicate<TideSchoolingLavaFish> predicate = other -> other.canBeFollowed() || !other.isFollower();
            List<? extends TideSchoolingLavaFish> nearbyFish = this.mob.level().getEntitiesOfClass(
                    this.mob.getClass(), this.mob.getBoundingBox().inflate(8.0, 8.0, 8.0), predicate);
            TideSchoolingLavaFish leader = DataFixUtils.orElse(nearbyFish.stream().filter(TideSchoolingLavaFish::canBeFollowed).findAny(), this.mob);
            leader.addFollowers(nearbyFish.stream().filter(other -> !other.isFollower() && other != this.mob));
            return this.mob.isFollower();
        }

        public boolean canContinueToUse() {
            return this.mob.isFollower() && this.mob.inRangeOfLeader();
        }

        public void start() {
            this.timeToRecalcPath = 0;
        }

        public void stop() {
            this.mob.stopFollowing();
        }

        public void tick() {
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = this.adjustedTickDelay(10);
                this.mob.pathToLeader();
            }
        }
    }
}
