package com.li64.tide.registries.entities.fish;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.level.Level;

public class SmoothSwimmingFish extends AbstractTideFish {
    public SmoothSwimmingFish(EntityType<? extends SmoothSwimmingFish> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SmoothSwimmingMoveControl(this, 45, 5,
                0.8f, 0.1f, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new net.minecraft.world.entity.ai.goal.TryFindLiquidGoal(this, net.minecraft.tags.FluidTags.WATER));
        this.goalSelector.addGoal(1, new RandomSwimmingGoal(this, 1.0f, 10));
        this.goalSelector.addGoal(1, new RandomLookAroundGoal(this));
    }
}
