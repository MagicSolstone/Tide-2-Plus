package net.minecraft.server.players;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import java.util.Optional;
import java.util.UUID;

public interface ProfileResolver {
   Optional<GameProfile> fetchByName(String name);

   Optional<GameProfile> fetchById(UUID id);

   default Optional<GameProfile> fetchByNameOrId(final Either<String, UUID> nameOrId) {
      return (Optional<GameProfile>)nameOrId.map(this::fetchByName, this::fetchById);
   }
}
