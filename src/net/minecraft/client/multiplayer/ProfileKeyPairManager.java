package net.minecraft.client.multiplayer;

import com.mojang.authlib.minecraft.UserApiService;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.User;
import net.minecraft.world.entity.player.ProfileKeyPair;

@Environment(EnvType.CLIENT)
public interface ProfileKeyPairManager {
   ProfileKeyPairManager EMPTY_KEY_MANAGER = new ProfileKeyPairManager$1();

   static ProfileKeyPairManager create(final UserApiService userApiService, final User user, final Path gameDirectory) {
      return new AccountProfileKeyPairManager(userApiService, user.getProfileId(), gameDirectory);
   }

   CompletableFuture<Optional<ProfileKeyPair>> prepareKeyPair();

   boolean shouldRefreshKeyPair();
}
