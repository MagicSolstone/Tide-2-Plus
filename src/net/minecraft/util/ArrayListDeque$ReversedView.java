package net.minecraft.util;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

class ArrayListDeque$ReversedView extends AbstractList<T> implements ListAndDeque<T> {
   private final ArrayListDeque<T> source;

   public ArrayListDeque$ReversedView(final ArrayListDeque<T> this$0, final ArrayListDeque source) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.source = source;
   }

   @Override
   public ListAndDeque<T> reversed() {
      return this.source;
   }

   @Override
   public T getFirst() {
      return (T)this.source.getLast();
   }

   @Override
   public T getLast() {
      return (T)this.source.getFirst();
   }

   @Override
   public void addFirst(final T t) {
      this.source.addLast(t);
   }

   @Override
   public void addLast(final T t) {
      this.source.addFirst(t);
   }

   @Override
   public boolean offerFirst(final T t) {
      return this.source.offerLast(t);
   }

   @Override
   public boolean offerLast(final T t) {
      return this.source.offerFirst(t);
   }

   @Nullable
   @Override
   public T pollFirst() {
      return (T)this.source.pollLast();
   }

   @Nullable
   @Override
   public T pollLast() {
      return (T)this.source.pollFirst();
   }

   @Nullable
   @Override
   public T peekFirst() {
      return (T)this.source.peekLast();
   }

   @Nullable
   @Override
   public T peekLast() {
      return (T)this.source.peekFirst();
   }

   @Override
   public T removeFirst() {
      return (T)this.source.removeLast();
   }

   @Override
   public T removeLast() {
      return (T)this.source.removeFirst();
   }

   @Override
   public boolean removeFirstOccurrence(final Object o) {
      return this.source.removeLastOccurrence(o);
   }

   @Override
   public boolean removeLastOccurrence(final Object o) {
      return this.source.removeFirstOccurrence(o);
   }

   @Override
   public Iterator<T> descendingIterator() {
      return this.source.iterator();
   }

   @Override
   public int size() {
      return this.source.size();
   }

   @Override
   public boolean isEmpty() {
      return this.source.isEmpty();
   }

   @Override
   public boolean contains(final Object o) {
      return this.source.contains(o);
   }

   @Override
   public T get(final int index) {
      return (T)this.source.get(this.reverseIndex(index));
   }

   @Override
   public T set(final int index, final T element) {
      return (T)this.source.set(this.reverseIndex(index), element);
   }

   @Override
   public void add(final int index, final T element) {
      this.source.add(this.reverseIndex(index) + 1, element);
   }

   @Override
   public T remove(final int index) {
      return (T)this.source.remove(this.reverseIndex(index));
   }

   @Override
   public int indexOf(final Object o) {
      return this.reverseIndex(this.source.lastIndexOf(o));
   }

   @Override
   public int lastIndexOf(final Object o) {
      return this.reverseIndex(this.source.indexOf(o));
   }

   @Override
   public List<T> subList(final int fromIndex, final int toIndex) {
      return this.source.subList(this.reverseIndex(toIndex) + 1, this.reverseIndex(fromIndex) + 1).reversed();
   }

   @Override
   public Iterator<T> iterator() {
      return this.source.descendingIterator();
   }

   @Override
   public void clear() {
      this.source.clear();
   }

   private int reverseIndex(final int index) {
      return index == -1 ? -1 : this.source.size() - 1 - index;
   }
}
