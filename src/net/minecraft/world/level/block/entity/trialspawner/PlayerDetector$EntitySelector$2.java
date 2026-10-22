package net.minecraft.world.level.block.entity.trialspawner;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;

class PlayerDetector$EntitySelector$2 implements PlayerDetector$EntitySelector {
   PlayerDetector$EntitySelector$2(final List val$players) {
      this.val$players = val$playersx;
   }

   @Override
   public List<Player> getPlayers(final ServerLevel level, final Predicate<? super Player> selector) {
      return this.val$players.stream().filter(selector).toList();
   }

   @Override
   public <T extends Entity> List<T> getEntities(
      final ServerLevel level, final EntityTypeTest<Entity, T> type, final AABB bb, final Predicate<? super T> selector
   ) {
      return this.val$players.stream().map(type::tryCast).filter(Objects::nonNull).filter(selector).toList();
   }
}
