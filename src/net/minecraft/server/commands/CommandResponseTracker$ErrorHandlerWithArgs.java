package net.minecraft.server.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

@FunctionalInterface
public interface CommandResponseTracker$ErrorHandlerWithArgs<Arg0, Arg1> {
   CommandSyntaxException get(Arg0 argument0, Arg1 argument1);
}
