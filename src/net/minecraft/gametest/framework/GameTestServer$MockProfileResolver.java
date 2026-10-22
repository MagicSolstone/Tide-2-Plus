package net.minecraft.gametest.framework;

import com.mojang.authlib.GameProfile;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.server.players.ProfileResolver;

class GameTestServer$MockProfileResolver implements ProfileResolver {
   private GameTestServer$MockProfileResolver() {
   }

   @Override
   public Optional<GameProfile> fetchByName(final String name) {
      return Optional.empty();
   }

   @Override
   public Optional<GameProfile> fetchById(final UUID id) {
      return Optional.empty();
   }
}
