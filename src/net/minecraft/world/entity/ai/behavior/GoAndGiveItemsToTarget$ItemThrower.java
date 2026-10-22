package net.minecraft.world.entity.ai.behavior;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

@FunctionalInterface
public interface GoAndGiveItemsToTarget$ItemThrower<E> {
   void throwItem(ServerLevel level, E thrower, Vec3 targetPos);
}
