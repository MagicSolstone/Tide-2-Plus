package net.minecraft.server.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.List;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
interface LootCommand$DropConsumer {
   void accept(CommandContext<CommandSourceStack> context, List<ItemStack> drops, CommandResponseTracker<ItemStack> response) throws CommandSyntaxException;
}
