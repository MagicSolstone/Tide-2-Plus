package net.minecraft.util.parsing.packrat;

class Term$3 implements Term<S> {
   Term$3(final Object val$message) {
      this.val$message = val$messagex;
   }

   @Override
   public boolean parse(final ParseState<S> state, final Scope scope, final Control control) {
      state.errorCollector().store(state.mark(), this.val$message);
      return false;
   }

   @Override
   public String toString() {
      return "fail";
   }
}
