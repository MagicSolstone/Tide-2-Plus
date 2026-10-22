package net.minecraft.util.parsing.packrat;

import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface Rule$RuleAction<S, T> {
   @Nullable
   T run(ParseState<S> state);
}
