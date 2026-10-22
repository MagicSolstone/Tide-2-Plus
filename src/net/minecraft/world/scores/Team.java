package net.minecraft.world.scores;

import java.util.Collection;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jspecify.annotations.Nullable;

public abstract class Team {
   public boolean isAlliedTo(@Nullable final Team other) {
      return other == null ? false : this == other;
   }

   public abstract String getName();

   public abstract MutableComponent getFormattedName(Component teamMemberName);

   public abstract boolean canSeeFriendlyInvisibles();

   public abstract boolean isAllowFriendlyFire();

   public abstract Team$Visibility getNameTagVisibility();

   public abstract Optional<TeamColor> getColor();

   public abstract Collection<String> getPlayers();

   public abstract Team$Visibility getDeathMessageVisibility();

   public abstract Team$CollisionRule getCollisionRule();
}
