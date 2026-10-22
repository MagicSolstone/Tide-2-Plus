package net.minecraft.server.commands;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.CompoundTagArgument;
import net.minecraft.nbt.CompoundTag;

class FunctionCommand$4 extends FunctionCommand$FunctionCustomExecutor {
   @Override
   protected CompoundTag arguments(final CommandContext<CommandSourceStack> context) {
      return CompoundTagArgument.getCompoundTag(context, "arguments");
   }
}
