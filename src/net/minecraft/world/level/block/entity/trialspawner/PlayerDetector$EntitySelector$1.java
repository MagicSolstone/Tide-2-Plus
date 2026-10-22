package net.minecraft.world.level.block.entity.trialspawner;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;

class PlayerDetector$EntitySelector$1 implements PlayerDetector$EntitySelector {
   @Override
   public List<ServerPlayer> getPlayers(final ServerLevel level, final Predicate<? super Player> selector) {
      return level.getPlayers(selector);
   }

   @Override
   public <T extends Entity> List<T> getEntities(
      final ServerLevel level, final EntityTypeTest<Entity, T> type, final AABB aabb, final Predicate<? super T> selector
   ) {
      return level.getEntities(type, aabb, selector);
   }
}
