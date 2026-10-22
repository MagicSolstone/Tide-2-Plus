package net.minecraft.server.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

class ArgProvider$1 implements ArgProvider<T> {
   ArgProvider$1(final InCommandFunction val$access, final String val$key, final Supplier val$child) {
      this.val$access = val$accessx;
      this.val$key = val$keyx;
      this.val$child = val$childx;
   }

   @Override
   public T access(final CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      return (T)this.val$access.apply(context);
   }

   @Override
   public ArgumentBuilder<CommandSourceStack, ?> wrap(
      final ArgumentBuilder<CommandSourceStack, ?> parent,
      final Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>> function
   ) {
      return parent.then(Commands.literal(this.val$key).then(function.apply((ArgumentBuilder<CommandSourceStack, ?>)this.val$child.get())));
   }
}
