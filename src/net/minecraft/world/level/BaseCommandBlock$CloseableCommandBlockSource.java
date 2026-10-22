package net.minecraft.world.level;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import net.minecraft.commands.CommandSource;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gamerules.GameRules;

public class BaseCommandBlock$CloseableCommandBlockSource implements CommandSource, AutoCloseable {
   private final ServerLevel level;
   private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.ROOT);
   private boolean closed;

   protected BaseCommandBlock$CloseableCommandBlockSource(final BaseCommandBlock this$0, final ServerLevel level) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.level = level;
   }

   @Override
   public boolean acceptsSuccess() {
      return !this.closed && this.level.getGameRules().get(GameRules.SEND_COMMAND_FEEDBACK);
   }

   @Override
   public boolean acceptsFailure() {
      return !this.closed;
   }

   @Override
   public boolean shouldInformAdmins() {
      return !this.closed && this.level.getGameRules().get(GameRules.COMMAND_BLOCK_OUTPUT);
   }

   @Override
   public void sendSystemMessage(final Component message) {
      if (!this.closed) {
         this.this$0.lastOutput = Component.literal("[" + TIME_FORMAT.format(ZonedDateTime.now()) + "] ").append(message);
         this.this$0.onUpdated(this.level);
      }
   }

   @Override
   public void close() throws Exception {
      this.closed = true;
   }
}
