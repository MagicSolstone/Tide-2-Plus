package net.minecraft.util.parsing.packrat;

import org.jspecify.annotations.Nullable;

public interface Rule<S, T> {
   @Nullable
   T parse(ParseState<S> state);

   static <S, T> Rule<S, T> fromTerm(final Term<S> child, final Rule$RuleAction<S, T> action) {
      return new Rule$WrappedTerm<>(action, child);
   }

   static <S, T> Rule<S, T> fromTerm(final Term<S> child, final Rule$SimpleRuleAction<S, T> action) {
      return new Rule$WrappedTerm<>(action, child);
   }
}
