package net.minecraft.client;

import com.mojang.serialization.Codec;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record OptionInstance$IntRange(int minInclusive, int maxInclusive, boolean applyValueImmediately) implements OptionInstance$IntRangeBase {
   public OptionInstance$IntRange(final int minInclusive, final int maxInclusive) {
      this(minInclusive, maxInclusive, true);
   }

   public Optional<Integer> validateValue(final Integer value) {
      return value.compareTo(this.minInclusive()) >= 0 && value.compareTo(this.maxInclusive()) <= 0 ? Optional.of(value) : Optional.empty();
   }

   @Override
   public Codec<Integer> codec() {
      return Codec.intRange(this.minInclusive, this.maxInclusive + 1);
   }
}
