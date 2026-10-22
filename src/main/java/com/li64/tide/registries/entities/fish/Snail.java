package com.li64.tide.registries.entities.fish;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class Snail extends AmphibiousFish {
    public Snail(EntityType<? extends Snail> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void handleAirSupply(net.minecraft.server.level.ServerLevel level, int airSupply) {
        this.setAirSupply(300);
    }

    @Override
    public float getSpeed() {
        return super.getSpeed() * (isInWater() ? 3.5f : 1.0f);
    }
}
