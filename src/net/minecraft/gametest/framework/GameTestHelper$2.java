package net.minecraft.gametest.framework;

import com.mojang.authlib.GameProfile;
import java.util.Objects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;

class GameTestHelper$2 extends ServerPlayer {
   GameTestHelper$2(
      final GameTestHelper this$0,
      final MinecraftServer server,
      final ServerLevel level,
      final GameProfile gameProfile,
      final ClientInformation clientInformation,
      final GameType val$gameType
   ) {
      this.val$gameType = val$gameTypex;
      Objects.requireNonNull(this$0);
      super(server, level, gameProfile, clientInformation);
   }

   @Override
   public GameType gameMode() {
      return this.val$gameType;
   }

   @Override
   public boolean isClientAuthoritative() {
      return false;
   }
}
