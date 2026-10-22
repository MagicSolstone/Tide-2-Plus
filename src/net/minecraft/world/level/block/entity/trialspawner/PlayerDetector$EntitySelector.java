package net.minecraft.world.level.block.entity.trialspawner;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;

public interface PlayerDetector$EntitySelector {
   PlayerDetector$EntitySelector SELECT_FROM_LEVEL = new PlayerDetector$EntitySelector$1();

   List<? extends Player> getPlayers(final ServerLevel level, final Predicate<? super Player> selector);

   <T extends Entity> List<T> getEntities(final ServerLevel level, final EntityTypeTest<Entity, T> type, final AABB bb, final Predicate<? super T> selector);

   static PlayerDetector$EntitySelector onlySelectPlayer(final Player player) {
      return onlySelectPlayers(List.of(player));
   }

   static PlayerDetector$EntitySelector onlySelectPlayers(final List<Player> players) {
      return new PlayerDetector$EntitySelector$2(players);
   }
}
