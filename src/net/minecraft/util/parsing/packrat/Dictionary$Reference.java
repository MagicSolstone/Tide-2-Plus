package net.minecraft.util.parsing.packrat;

record Dictionary$Reference<S, T>(Dictionary$Entry<S, T> ruleToParse, Atom<T> nameToStore) implements Term<S> {
   @Override
   public boolean parse(final ParseState<S> state, final Scope scope, final Control control) {
      T result = state.parse(this.ruleToParse);
      if (result == null) {
         return false;
      } else {
         scope.put(this.nameToStore, result);
         return true;
      }
   }
}
