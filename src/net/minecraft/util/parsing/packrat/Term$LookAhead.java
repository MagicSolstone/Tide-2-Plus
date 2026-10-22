package net.minecraft.util.parsing.packrat;

public record Term$LookAhead<S>(Term<S> term, boolean positive) implements Term<S> {
   @Override
   public boolean parse(final ParseState<S> state, final Scope scope, final Control control) {
      int mark = state.mark();
      boolean result = this.term.parse(state.silent(), scope, control);
      state.restore(mark);
      return this.positive == result;
   }
}
