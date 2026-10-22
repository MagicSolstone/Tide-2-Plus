package net.minecraft.commands.synchronization;

import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.CommandBuildContext;

public interface ArgumentTypeInfo$Template<A extends ArgumentType<?>> {
   A instantiate(CommandBuildContext context);

   ArgumentTypeInfo<A, ?> type();
}
