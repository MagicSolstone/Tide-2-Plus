package net.minecraft.util.parsing.packrat;

@FunctionalInterface
public interface Rule$SimpleRuleAction<S, T> extends Rule$RuleAction<S, T> {
   T run(Scope ruleScope);

   @Override
   default T run(final ParseState<S> state) {
      return this.run(state.scope());
   }
}
