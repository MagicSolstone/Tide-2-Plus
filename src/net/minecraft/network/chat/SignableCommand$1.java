package net.minecraft.network.chat;

import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import java.util.List;
import net.minecraft.commands.ArgumentVisitor$Output;
import net.minecraft.commands.arguments.SignedArgument;
import org.jspecify.annotations.Nullable;

class SignableCommand$1 implements ArgumentVisitor$Output<S> {
   SignableCommand$1(final String val$commandString, final List val$arguments) {
      this.val$commandString = val$commandStringx;
      this.val$arguments = val$argumentsx;
   }

   @Override
   public <T> void accept(final CommandContextBuilder<S> context, final ArgumentCommandNode<S, T> argument, @Nullable final ParsedArgument<S, T> value) {
      if (value != null && argument.getType() instanceof SignedArgument) {
         String stringValue = value.getRange().get(this.val$commandString);
         this.val$arguments.add(new SignableCommand$Argument(argument, stringValue));
      }
   }
}
