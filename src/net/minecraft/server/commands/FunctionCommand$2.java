package net.minecraft.server.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.commands.data.DataAccessor;

class FunctionCommand$2 extends FunctionCommand$FunctionCustomExecutor {
   FunctionCommand$2(final ArgProvider val$provider) {
      this.val$provider = val$providerx;
   }

   @Override
   protected CompoundTag arguments(final CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      return FunctionCommand.getArgumentTag(NbtPathArgument.getPath(context, "path"), (DataAccessor)this.val$provider.access(context));
   }
}
