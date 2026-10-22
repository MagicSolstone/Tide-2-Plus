package net.minecraft.util;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jspecify.annotations.Nullable;

public class StringRepresentable$EnumCodec<E extends Enum<E> & StringRepresentable> extends StringRepresentable$StringRepresentableCodec<E> {
   private final Function<String, E> resolver;

   public StringRepresentable$EnumCodec(final E[] valueArray, final Function<String, E> nameResolver) {
      super(valueArray, nameResolver, rec$ -> rec$.ordinal());
      this.resolver = nameResolver;
   }

   @Nullable
   public E byName(final String name) {
      return this.resolver.apply(name);
   }

   public E byName(final String name, final E _default) {
      return Objects.requireNonNullElse(this.byName(name), _default);
   }

   public E byName(final String name, final Supplier<? extends E> defaultSupplier) {
      return Objects.requireNonNullElseGet(this.byName(name), defaultSupplier);
   }
}
