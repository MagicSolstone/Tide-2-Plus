package net.minecraft.server.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

@FunctionalInterface
public interface CommandResponseTracker$ErrorHandler {
   CommandSyntaxException get();
}
