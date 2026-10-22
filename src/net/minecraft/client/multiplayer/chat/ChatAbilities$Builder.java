package net.minecraft.client.multiplayer.chat;

import java.util.HashSet;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ChatAbilities$Builder {
   private final Set<ChatRestriction> restrictions = new HashSet<>();

   public ChatAbilities$Builder addRestriction(final ChatRestriction restriction) {
      this.restrictions.add(restriction);
      return this;
   }

   public ChatAbilities build() {
      return this.restrictions.isEmpty() ? ChatAbilities.NO_RESTRICTIONS : new ChatAbilities(Set.copyOf(this.restrictions));
   }
}
