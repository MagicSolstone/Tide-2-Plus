package net.minecraft.util.parsing.packrat;

import java.util.ArrayList;
import java.util.List;

public record Term$Repeated<S, T>(NamedRule<S, T> element, Atom<List<T>> listName, int minRepetitions) implements Term<S> {
   @Override
   public boolean parse(final ParseState<S> state, final Scope scope, final Control control) {
      int mark = state.mark();
      List<T> elements = new ArrayList<>(this.minRepetitions);

      while (true) {
         int entryMark = state.mark();
         T parsedElement = state.parse(this.element);
         if (parsedElement == null) {
            state.restore(entryMark);
            if (elements.size() < this.minRepetitions) {
               state.restore(mark);
               return false;
            } else {
               scope.put(this.listName, elements);
               return true;
            }
         }

         elements.add(parsedElement);
      }
   }
}
