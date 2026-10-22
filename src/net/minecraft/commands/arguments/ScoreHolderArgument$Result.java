package net.minecraft.commands.arguments;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Collection;
import java.util.function.Supplier;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.scores.ScoreHolder;

@FunctionalInterface
public interface ScoreHolderArgument$Result {
   Collection<ScoreHolder> getNames(final CommandSourceStack sender, Supplier<Collection<ScoreHolder>> wildcard) throws CommandSyntaxException;
}
