package net.minecraft.util.parsing.packrat;

public record Term$Alternative<S>(Term<S>[] elements) implements Term<S> {
   @Override
   public boolean parse(final ParseState<S> state, final Scope scope, final Control control) {
      Control controlForThis = state.acquireControl();

      try {
         int mark = state.mark();
         scope.splitFrame();

         for (Term<S> element : this.elements) {
            if (element.parse(state, scope, controlForThis)) {
               scope.mergeFrame();
               return true;
            }

            scope.clearFrameValues();
            state.restore(mark);
            if (controlForThis.hasCut()) {
               break;
            }
         }

         scope.popFrame();
         return false;
      } finally {
         state.releaseControl();
      }
   }
}
