package net.minecraft.commands.arguments.selector.options;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;

@FunctionalInterface
public interface EntitySelectorOptions$Modifier {
   void handle(EntitySelectorParser parser) throws CommandSyntaxException;
}
