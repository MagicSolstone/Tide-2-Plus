package net.minecraft.server.commands;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import org.jspecify.annotations.Nullable;

class FunctionCommand$3 extends FunctionCommand$FunctionCustomExecutor {
   @Nullable
   @Override
   protected CompoundTag arguments(final CommandContext<CommandSourceStack> context) {
      return null;
   }
}
