package net.minecraft.client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.Optional;
import java.util.function.IntSupplier;
import java.util.stream.IntStream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.CycleButton$ValueListSupplier;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public record OptionInstance$ClampingLazyMaxIntRange(int minInclusive, IntSupplier maxSupplier, int encodableMaxInclusive)
   implements OptionInstance$IntRangeBase,
   OptionInstance$SliderableOrCyclableValueSet<Integer> {
   public Optional<Integer> validateValue(final Integer value) {
      return Optional.of(Mth.clamp(value, this.minInclusive(), this.maxInclusive()));
   }

   @Override
   public int maxInclusive() {
      return this.maxSupplier.getAsInt();
   }

   @Override
   public Codec<Integer> codec() {
      return Codec.INT
         .validate(
            value -> {
               int maxExclusive = this.encodableMaxInclusive + 1;
               return value.compareTo(this.minInclusive) >= 0 && value.compareTo(maxExclusive) <= 0
                  ? DataResult.success(value)
                  : DataResult.error(() -> "Value " + value + " outside of range [" + this.minInclusive + ":" + maxExclusive + "]", value);
            }
         );
   }

   @Override
   public boolean createCycleButton() {
      return true;
   }

   @Override
   public CycleButton$ValueListSupplier<Integer> valueListSupplier() {
      return CycleButton$ValueListSupplier.create(IntStream.range(this.minInclusive, this.maxInclusive() + 1).boxed().toList());
   }
}
