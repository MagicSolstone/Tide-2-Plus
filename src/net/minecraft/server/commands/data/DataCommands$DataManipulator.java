package net.minecraft.server.commands.data;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.List;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.NbtPathArgument$NbtPath;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

@FunctionalInterface
interface DataCommands$DataManipulator {
   int modify(CommandContext<CommandSourceStack> context, CompoundTag targetData, NbtPathArgument$NbtPath targetPath, List<Tag> source) throws CommandSyntaxException;
}
