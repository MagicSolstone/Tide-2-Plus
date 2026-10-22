package net.minecraft.server.commands.data;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

@FunctionalInterface
interface DataCommands$DataManipulatorDecorator {
   ArgumentBuilder<CommandSourceStack, ?> create(DataCommands$DataManipulator manipulator);
}
