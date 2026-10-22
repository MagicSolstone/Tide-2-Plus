package net.minecraft.world.entity;

import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface EntityType$EntityFactory<T extends Entity> {
   @Nullable
   T create(final EntityType<T> entityType, final Level level);
}
