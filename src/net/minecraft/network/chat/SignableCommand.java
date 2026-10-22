package net.minecraft.network.chat;

import com.mojang.brigadier.ParseResults;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.commands.ArgumentVisitor;
import org.jspecify.annotations.Nullable;

public record SignableCommand<S>(List<SignableCommand$Argument<S>> arguments) {
   public static <S> boolean hasSignableArguments(final ParseResults<S> command) {
      return !of(command).arguments().isEmpty();
   }

   public static <S> SignableCommand<S> of(final ParseResults<S> command) {
      String commandString = command.getReader().getString();
      List<SignableCommand$Argument<S>> arguments = new ArrayList<>();
      ArgumentVisitor.visitArguments(command, new SignableCommand$1(commandString, arguments), true);
      return new SignableCommand<>(arguments);
   }

   @Nullable
   public SignableCommand$Argument<S> getArgument(final String name) {
      for (SignableCommand$Argument<S> argument : this.arguments) {
         if (name.equals(argument.name())) {
            return argument;
         }
      }

      return null;
   }
}
