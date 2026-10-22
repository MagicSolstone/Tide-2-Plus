package net.minecraft.commands;

import net.minecraft.network.chat.PlayerChatMessage;
import org.jspecify.annotations.Nullable;

public interface CommandSigningContext {
   CommandSigningContext ANONYMOUS = new CommandSigningContext$1();

   @Nullable
   PlayerChatMessage getArgument(String name);
}
