package net.minecraft.server.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

public record CommandResponseTracker$MessagesWithArgs<Element, Arg0, Arg1>(
   @Nullable CommandResponseTracker$ErrorHandlerWithArgs<Arg0, Arg1> onZero, CommandResponseTracker$DispatchWithArgs<Component, Element, Arg0, Arg1> onSuccess
) {
   public void throwIfZero(final int value, final Arg0 argument0, final Arg1 argument1) throws CommandSyntaxException {
      if (this.onZero != null && value == 0) {
         throw this.onZero.get(argument0, argument1);
      }
   }
}
