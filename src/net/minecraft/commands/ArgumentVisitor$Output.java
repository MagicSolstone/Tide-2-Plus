package net.minecraft.commands;

import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface ArgumentVisitor$Output<S> {
   <T> void accept(CommandContextBuilder<S> context, ArgumentCommandNode<S, T> argument, @Nullable final ParsedArgument<S, T> value);
}
