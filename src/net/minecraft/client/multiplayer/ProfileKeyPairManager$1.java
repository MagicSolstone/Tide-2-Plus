package net.minecraft.client.multiplayer;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.player.ProfileKeyPair;

@Environment(EnvType.CLIENT)
class ProfileKeyPairManager$1 implements ProfileKeyPairManager {
   @Override
   public CompletableFuture<Optional<ProfileKeyPair>> prepareKeyPair() {
      return CompletableFuture.completedFuture(Optional.empty());
   }

   @Override
   public boolean shouldRefreshKeyPair() {
      return false;
   }
}
