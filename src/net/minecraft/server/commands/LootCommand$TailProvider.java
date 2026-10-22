package net.minecraft.server.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

@FunctionalInterface
interface LootCommand$TailProvider {
   ArgumentBuilder<CommandSourceStack, ?> construct(final ArgumentBuilder<CommandSourceStack, ?> root, LootCommand$DropConsumer consumer);
}
