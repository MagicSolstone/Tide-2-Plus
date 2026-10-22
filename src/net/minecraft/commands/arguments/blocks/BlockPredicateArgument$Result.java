package net.minecraft.commands.arguments.blocks;

import java.util.function.Predicate;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public interface BlockPredicateArgument$Result extends Predicate<BlockInWorld> {
   boolean requiresNbt();
}
