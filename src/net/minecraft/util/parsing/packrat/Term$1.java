package net.minecraft.util.parsing.packrat;

class Term$1 implements Term<S> {
   @Override
   public boolean parse(final ParseState<S> state, final Scope scope, final Control control) {
      control.cut();
      return true;
   }

   @Override
   public String toString() {
      return "↑";
   }
}
