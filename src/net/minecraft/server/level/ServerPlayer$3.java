package net.minecraft.server.level;

import java.util.Objects;
import net.minecraft.commands.CommandSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.gamerules.GameRules;

class ServerPlayer$3 implements CommandSource {
   ServerPlayer$3(final ServerPlayer this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public boolean acceptsSuccess() {
      return this.this$0.level().getGameRules().get(GameRules.SEND_COMMAND_FEEDBACK);
   }

   @Override
   public boolean acceptsFailure() {
      return true;
   }

   @Override
   public boolean shouldInformAdmins() {
      return true;
   }

   @Override
   public void sendSystemMessage(final Component message) {
      this.this$0.sendSystemMessage(message);
   }
}
