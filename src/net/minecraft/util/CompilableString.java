package net.minecraft.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.Objects;
import java.util.function.Function;

public class CompilableString<T> {
   private final String source;
   private final T compiled;

   private CompilableString(final String source, final T compiled) {
      this.source = source;
      this.compiled = compiled;
   }

   public static <T> Codec<CompilableString<T>> codec(final Function<String, DataResult<T>> compiler) {
      return Codec.STRING.comapFlatMap(s -> compiler.apply(s).map(compiled -> new CompilableString<>(s, compiled)), CompilableString::source);
   }

   public String source() {
      return this.source;
   }

   public T compiled() {
      return this.compiled;
   }

   @Override
   public boolean equals(final Object o) {
      return o instanceof CompilableString<?> that && Objects.equals(this.source, that.source);
   }

   @Override
   public int hashCode() {
      return this.source.hashCode();
   }

   @Override
   public String toString() {
      return this.source;
   }
}
