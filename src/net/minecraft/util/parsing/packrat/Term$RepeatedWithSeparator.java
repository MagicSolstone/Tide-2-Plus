package net.minecraft.util.parsing.packrat;

import java.util.ArrayList;
import java.util.List;

public record Term$RepeatedWithSeparator<S, T>(
   NamedRule<S, T> element, Atom<List<T>> listName, Term<S> separator, int minRepetitions, boolean allowTrailingSeparator
) implements Term<S> {
   @Override
   public boolean parse(final ParseState<S> state, final Scope scope, final Control control) {
      int listMark = state.mark();
      List<T> elements = new ArrayList<>(this.minRepetitions);
      boolean first = true;

      while (true) {
         int markBeforeSeparator = state.mark();
         if (!first && !this.separator.parse(state, scope, control)) {
            state.restore(markBeforeSeparator);
            break;
         }

         int markAfterSeparator = state.mark();
         T parsedElement = state.parse(this.element);
         if (parsedElement == null) {
            if (first) {
               state.restore(markAfterSeparator);
            } else {
               if (!this.allowTrailingSeparator) {
                  state.restore(listMark);
                  return false;
               }

               state.restore(markAfterSeparator);
            }
            break;
         }

         elements.add(parsedElement);
         first = false;
      }

      if (elements.size() < this.minRepetitions) {
         state.restore(listMark);
         return false;
      } else {
         scope.put(this.listName, elements);
         return true;
      }
   }
}
