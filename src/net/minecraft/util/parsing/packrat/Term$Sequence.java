package net.minecraft.util.parsing.packrat;

public record Term$Sequence<S>(Term<S>[] elements) implements Term<S> {
   @Override
   public boolean parse(final ParseState<S> state, final Scope scope, final Control control) {
      int mark = state.mark();

      for (Term<S> element : this.elements) {
         if (!element.parse(state, scope, control)) {
            state.restore(mark);
            return false;
         }
      }

      return true;
   }
}
