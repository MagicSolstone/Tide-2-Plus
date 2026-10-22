package net.minecraft.commands.arguments;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.players.NameAndId;

@FunctionalInterface
public interface GameProfileArgument$Result {
   Collection<NameAndId> getNames(final CommandSourceStack sender) throws CommandSyntaxException;
}
