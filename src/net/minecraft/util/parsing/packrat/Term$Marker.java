package net.minecraft.util.parsing.packrat;

public record Term$Marker<S, T>(Atom<T> name, T value) implements Term<S> {
   @Override
   public boolean parse(final ParseState<S> state, final Scope scope, final Control control) {
      scope.put(this.name, this.value);
      return true;
   }
}
