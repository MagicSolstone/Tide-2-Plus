package net.minecraft.gametest.framework;

import com.mojang.authlib.GameProfile;
import java.util.Objects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;

class GameTestHelper$1 extends Player {
   GameTestHelper$1(final GameTestHelper this$0, final Level level, final GameProfile gameProfile, final GameType val$gameType) {
      this.val$gameType = val$gameTypex;
      Objects.requireNonNull(this$0);
      super(level, gameProfile);
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
