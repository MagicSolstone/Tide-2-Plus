package net.minecraft.commands;

import java.util.Map;
import net.minecraft.network.chat.PlayerChatMessage;
import org.jspecify.annotations.Nullable;

public record CommandSigningContext$SignedArguments(Map<String, PlayerChatMessage> arguments) implements CommandSigningContext {
   @Nullable
   @Override
   public PlayerChatMessage getArgument(final String name) {
      return this.arguments.get(name);
   }
}
