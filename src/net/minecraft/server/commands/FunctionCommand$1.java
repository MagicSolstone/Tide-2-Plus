package net.minecraft.server.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.commands.data.DataAccessor;

class FunctionCommand$1 extends FunctionCommand$FunctionCustomExecutor {
   FunctionCommand$1(final ArgProvider val$provider) {
      this.val$provider = val$providerx;
   }

   @Override
   protected CompoundTag arguments(final CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      return ((DataAccessor)this.val$provider.access(context)).getData();
   }
}
