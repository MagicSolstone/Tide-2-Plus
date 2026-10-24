package com.li64.tide.mixin;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Lets a fish on a display count as in water (see FishDisplayRenderer). */
@Mixin(Entity.class)
public interface EntityWaterAccessor {
    @Accessor("wasTouchingWater")
    void tide$setWasTouchingWater(boolean inWater);
}
