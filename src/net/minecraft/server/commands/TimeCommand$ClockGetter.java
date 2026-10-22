package net.minecraft.server.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder;
import net.minecraft.world.clock.WorldClock;

interface TimeCommand$ClockGetter {
   Holder<WorldClock> getClock(CommandContext<CommandSourceStack> context) throws CommandSyntaxException;
}
