package net.minecraft.world.level.portal;

import net.minecraft.world.entity.Entity;

@FunctionalInterface
public interface TeleportTransition$PostTeleportTransition {
   void onTransition(final Entity entity);

   default TeleportTransition$PostTeleportTransition then(final TeleportTransition$PostTeleportTransition postTeleportTransition) {
      return entity -> {
         this.onTransition(entity);
         postTeleportTransition.onTransition(entity);
      };
   }
}
