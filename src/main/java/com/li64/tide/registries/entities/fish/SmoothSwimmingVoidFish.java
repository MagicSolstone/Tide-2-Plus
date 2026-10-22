package com.li64.tide.registries.entities.fish;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.animal.fish.AbstractSchoolingFish;
import net.minecraft.world.level.Level;

public class SmoothSwimmingVoidFish extends TideVoidFish {
    public SmoothSwimmingVoidFish(EntityType<? extends AbstractSchoolingFish> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SmoothSwimmingMoveControl(this, 45, 5,
                0.8f, 0.1f, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(4, new VoidFishSwimGoal(this));
    }
}
