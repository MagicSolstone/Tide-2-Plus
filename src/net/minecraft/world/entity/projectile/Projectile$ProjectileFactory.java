package net.minecraft.world.entity.projectile;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface Projectile$ProjectileFactory<T extends Projectile> {
   T create(final ServerLevel level, LivingEntity entity, ItemStack itemStack);
}
