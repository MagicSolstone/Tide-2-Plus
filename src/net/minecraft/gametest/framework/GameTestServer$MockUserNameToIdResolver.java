package net.minecraft.gametest.framework;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.UserNameToIdResolver;

class GameTestServer$MockUserNameToIdResolver implements UserNameToIdResolver {
   private final Set<NameAndId> savedIds = new HashSet<>();

   private GameTestServer$MockUserNameToIdResolver() {
   }

   @Override
   public void add(final NameAndId nameAndId) {
      this.savedIds.add(nameAndId);
   }

   @Override
   public Optional<NameAndId> get(final String name) {
      return this.savedIds.stream().filter(e -> e.name().equals(name)).findFirst().or(() -> Optional.of(NameAndId.createOffline(name)));
   }

   @Override
   public Optional<NameAndId> get(final UUID id) {
      return this.savedIds.stream().filter(e -> e.id().equals(id)).findFirst();
   }

   @Override
   public void resolveOfflineUsers(final boolean value) {
   }

   @Override
   public void save() {
   }
}
