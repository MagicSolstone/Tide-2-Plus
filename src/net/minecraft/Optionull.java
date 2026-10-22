package net.minecraft;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jspecify.annotations.Nullable;

public class Optionull {
   @Deprecated
   public static <T> T orElse(@Nullable final T t, final T defaultValue) {
      return Objects.requireNonNullElse(t, defaultValue);
   }

   @Nullable
   public static <T, R> R map(@Nullable final T t, final Function<T, R> map) {
      return t == null ? null : map.apply(t);
   }

   public static <T, R> R mapOrDefault(@Nullable final T t, final Function<T, R> map, final R defaultValue) {
      return t == null ? defaultValue : map.apply(t);
   }

   public static <T, R> R mapOrElse(@Nullable final T t, final Function<T, R> map, final Supplier<R> elseSupplier) {
      return t == null ? elseSupplier.get() : map.apply(t);
   }

   @Nullable
   public static <T> T first(final Collection<T> collection) {
      Iterator<T> iterator = collection.iterator();
      return iterator.hasNext() ? iterator.next() : null;
   }

   public static <T> T firstOrDefault(final Collection<T> collection, final T defaultValue) {
      Iterator<T> iterator = collection.iterator();
      return iterator.hasNext() ? iterator.next() : defaultValue;
   }

   public static <T> T firstOrElse(final Collection<T> collection, final Supplier<T> elseSupplier) {
      Iterator<T> iterator = collection.iterator();
      return iterator.hasNext() ? iterator.next() : elseSupplier.get();
   }

   public static <T> boolean isNullOrEmpty(@Nullable final T[] t) {
      return t == null || t.length == 0;
   }

   public static boolean isNullOrEmpty(@Nullable final boolean[] t) {
      return t == null || t.length == 0;
   }

   public static boolean isNullOrEmpty(@Nullable final byte[] t) {
      return t == null || t.length == 0;
   }

   public static boolean isNullOrEmpty(@Nullable final char[] t) {
      return t == null || t.length == 0;
   }

   public static boolean isNullOrEmpty(@Nullable final short[] t) {
      return t == null || t.length == 0;
   }

   public static boolean isNullOrEmpty(@Nullable final int[] t) {
      return t == null || t.length == 0;
   }

   public static boolean isNullOrEmpty(@Nullable final long[] t) {
      return t == null || t.length == 0;
   }

   public static boolean isNullOrEmpty(@Nullable final float[] t) {
      return t == null || t.length == 0;
   }

   public static boolean isNullOrEmpty(@Nullable final double[] t) {
      return t == null || t.length == 0;
   }
}
