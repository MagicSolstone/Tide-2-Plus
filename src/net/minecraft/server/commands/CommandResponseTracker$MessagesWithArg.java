package net.minecraft.server.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

public record CommandResponseTracker$MessagesWithArg<Element, Arg>(
   @Nullable CommandResponseTracker$ErrorHandlerWithArg<Arg> onZero, CommandResponseTracker$DispatchWithArg<Component, Element, Arg> onSuccess
) {
   public void throwIfZero(final int value, final Arg argument) throws CommandSyntaxException {
      if (this.onZero != null && value == 0) {
         throw this.onZero.get(argument);
      }
   }
}
