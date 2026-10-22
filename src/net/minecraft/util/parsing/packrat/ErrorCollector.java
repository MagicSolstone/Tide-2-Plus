package net.minecraft.util.parsing.packrat;

public interface ErrorCollector<S> {
   void store(int cursor, SuggestionSupplier<S> suggestions, Object reason);

   default void store(final int cursor, final Object reason) {
      this.store(cursor, SuggestionSupplier.empty(), reason);
   }

   void finish(int finalCursor);
}
