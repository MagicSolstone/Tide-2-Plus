package net.minecraft.util.parsing.packrat;

public record Term$Maybe<S>(Term<S> term) implements Term<S> {
   @Override
   public boolean parse(final ParseState<S> state, final Scope scope, final Control control) {
      int mark = state.mark();
      if (!this.term.parse(state, scope, control)) {
         state.restore(mark);
      }

      return true;
   }
}
