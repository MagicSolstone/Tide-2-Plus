package net.minecraft.server.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

public record CommandResponseTracker$Messages<Element>(
   @Nullable CommandResponseTracker$ErrorHandler onZero, CommandResponseTracker$Dispatch<Component, Element> onSuccess
) {
   public void throwIfZero(final int value) throws CommandSyntaxException {
      if (this.onZero != null && value == 0) {
         throw this.onZero.get();
      }
   }
}
