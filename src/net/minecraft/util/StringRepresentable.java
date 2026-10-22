package net.minecraft.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public interface StringRepresentable {
   int PRE_BUILT_MAP_THRESHOLD = 16;

   String getSerializedName();

   static <E extends Enum<E> & StringRepresentable> StringRepresentable$EnumCodec<E> fromEnum(final Supplier<E[]> values) {
      return fromEnumWithMapping(values, s -> s);
   }

   static <E extends Enum<E> & StringRepresentable> StringRepresentable$EnumCodec<E> fromEnumWithMapping(
      final Supplier<E[]> values, final Function<String, String> converter
   ) {
      E[] valueArray = (E[])values.get();
      Function<String, E> lookupFunction = createNameLookup(valueArray, e -> converter.apply(e.getSerializedName()));
      return new StringRepresentable$EnumCodec<>(valueArray, lookupFunction);
   }

   static <T extends StringRepresentable> Codec<T> fromValues(final Supplier<T[]> values) {
      T[] valueArray = (T[])values.get();
      Function<String, T> lookupFunction = createNameLookup(valueArray);
      ToIntFunction<T> indexLookup = Util.createIndexLookup(Arrays.asList(valueArray));
      return new StringRepresentable$StringRepresentableCodec<>(valueArray, lookupFunction, indexLookup);
   }

   static <T extends StringRepresentable> Function<String, T> createNameLookup(final T[] valueArray) {
      return createNameLookup(valueArray, StringRepresentable::getSerializedName);
   }

   static <T> Function<String, T> createNameLookup(final T[] valueArray, final Function<T, String> converter) {
      if (valueArray.length > 16) {
         Map<String, T> byName = Arrays.<T>stream(valueArray).collect(Collectors.toMap(converter, d -> (T)d));
         return byName::get;
      } else {
         return id -> {
            for (T value : valueArray) {
               if (converter.apply(value).equals(id)) {
                  return value;
               }
            }

            return null;
         };
      }
   }

   static Keyable keys(final StringRepresentable[] values) {
      return new StringRepresentable$1(values);
   }
}
