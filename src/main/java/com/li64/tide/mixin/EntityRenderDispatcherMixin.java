package com.li64.tide.mixin;

import com.li64.tide.data.FreezableMob;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/** A mob frozen by the Enchanted Pocket Watch is drawn without in-between-tick movement, so it holds still. */
@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @ModifyVariable(method = "extractEntity", at = @At("HEAD"), argsOnly = true)
    private float tide$freezePartialTick(float partialTick, Entity entity) {
        if (entity instanceof FreezableMob freezable && freezable.tide$isFrozen()
                && !(entity instanceof Mob mob && mob.isDeadOrDying())) return 0f;
        return partialTick;
    }
}
