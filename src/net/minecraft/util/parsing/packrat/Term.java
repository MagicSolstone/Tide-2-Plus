package net.minecraft.util.parsing.packrat;

import java.util.List;

public interface Term<S> {
   boolean parse(ParseState<S> state, Scope scope, Control control);

   static <S, T> Term<S> marker(final Atom<T> name, final T value) {
      return new Term$Marker<>(name, value);
   }

   @SafeVarargs
   static <S> Term<S> sequence(final Term<S>... terms) {
      return new Term$Sequence<>(terms);
   }

   @SafeVarargs
   static <S> Term<S> alternative(final Term<S>... terms) {
      return new Term$Alternative<>(terms);
   }

   static <S> Term<S> optional(final Term<S> term) {
      return new Term$Maybe<>(term);
   }

   static <S, T> Term<S> repeated(final NamedRule<S, T> element, final Atom<List<T>> listName) {
      return repeated(element, listName, 0);
   }

   static <S, T> Term<S> repeated(final NamedRule<S, T> element, final Atom<List<T>> listName, final int minRepetitions) {
      return new Term$Repeated<>(element, listName, minRepetitions);
   }

   static <S, T> Term<S> repeatedWithTrailingSeparator(final NamedRule<S, T> element, final Atom<List<T>> listName, final Term<S> separator) {
      return repeatedWithTrailingSeparator(element, listName, separator, 0);
   }

   static <S, T> Term<S> repeatedWithTrailingSeparator(
      final NamedRule<S, T> element, final Atom<List<T>> listName, final Term<S> separator, final int minRepetitions
   ) {
      return new Term$RepeatedWithSeparator<>(element, listName, separator, minRepetitions, true);
   }

   static <S, T> Term<S> repeatedWithoutTrailingSeparator(final NamedRule<S, T> element, final Atom<List<T>> listName, final Term<S> separator) {
      return repeatedWithoutTrailingSeparator(element, listName, separator, 0);
   }

   static <S, T> Term<S> repeatedWithoutTrailingSeparator(
      final NamedRule<S, T> element, final Atom<List<T>> listName, final Term<S> separator, final int minRepetitions
   ) {
      return new Term$RepeatedWithSeparator<>(element, listName, separator, minRepetitions, false);
   }

   static <S> Term<S> positiveLookahead(final Term<S> term) {
      return new Term$LookAhead<>(term, true);
   }

   static <S> Term<S> negativeLookahead(final Term<S> term) {
      return new Term$LookAhead<>(term, false);
   }

   static <S> Term<S> cut() {
      return new Term$1();
   }

   static <S> Term<S> empty() {
      return new Term$2();
   }

   static <S> Term<S> fail(final Object message) {
      return new Term$3(message);
   }
}
