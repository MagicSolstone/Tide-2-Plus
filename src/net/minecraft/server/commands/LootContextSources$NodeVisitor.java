package net.minecraft.server.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import java.util.function.Consumer;
import net.minecraft.commands.CommandSourceStack;

@FunctionalInterface
public interface LootContextSources$NodeVisitor {
   void visit(LootContextSources$ContextDecorator contextDecorator, Consumer<ArgumentBuilder<CommandSourceStack, ?>> output);
}
