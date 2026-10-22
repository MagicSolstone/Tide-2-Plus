package net.minecraft.util.thread;

import java.util.Map;
import java.util.function.BiFunction;
import org.jspecify.annotations.Nullable;

record ParallelMapTransform$Container<K, U, V>(BiFunction<K, U, V> operation, Object[] keys, Object[] values) {
   public ParallelMapTransform$Container(final BiFunction<K, U, V> operation, final int size) {
      this(operation, new Object[size], new Object[size]);
   }

   public void put(final int index, final K key, final U input) {
      this.keys[index] = key;
      this.values[index] = input;
   }

   @Nullable
   private K key(final int index) {
      return (K)this.keys[index];
   }

   @Nullable
   private V output(final int index) {
      return (V)this.values[index];
   }

   @Nullable
   private U input(final int index) {
      return (U)this.values[index];
   }

   public void applyOperation(final int index) {
      this.values[index] = this.operation.apply(this.key(index), this.input(index));
   }

   public void copyOut(final int index, final Map<K, V> output) {
      V value = this.output(index);
      if (value != null) {
         K key = this.key(index);
         output.put(key, value);
      }
   }

   public int size() {
      return this.keys.length;
   }
}
