package net.minecraft.util.parsing.packrat;

import org.jspecify.annotations.Nullable;

public record Rule$WrappedTerm<S, T>(Rule$RuleAction<S, T> action, Term<S> child) implements Rule<S, T> {
   @Nullable
   @Override
   public T parse(final ParseState<S> state) {
      Scope scope = state.scope();
      scope.pushFrame();

      Object var3;
      try {
         if (!this.child.parse(state, scope, Control.UNBOUND)) {
            return null;
         }

         var3 = this.action.run(state);
      } finally {
         scope.popFrame();
      }

      return (T)var3;
   }
}
