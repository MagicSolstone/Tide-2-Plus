package net.minecraft.commands.arguments;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.world.scores.ScoreAccess;

@FunctionalInterface
public interface OperationArgument$Operation {
   void apply(ScoreAccess a, ScoreAccess b) throws CommandSyntaxException;
}
