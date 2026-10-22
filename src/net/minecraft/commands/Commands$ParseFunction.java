package net.minecraft.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

@FunctionalInterface
public interface Commands$ParseFunction {
   void parse(StringReader value) throws CommandSyntaxException;
}
