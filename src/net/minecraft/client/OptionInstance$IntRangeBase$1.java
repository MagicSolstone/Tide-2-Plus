package net.minecraft.client;

import com.mojang.serialization.Codec;
import java.util.Objects;
import java.util.Optional;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class OptionInstance$IntRangeBase$1 implements OptionInstance$SliderableValueSet<R> {
   OptionInstance$IntRangeBase$1(final OptionInstance$IntRangeBase this$0, final ToIntFunction val$from, final IntFunction val$to, final boolean val$discrete) {
      this.val$from = val$fromx;
      this.val$to = val$tox;
      this.val$discrete = val$discretex;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public Optional<R> validateValue(final R value) {
      return this.this$0.validateValue(this.val$from.applyAsInt(value)).map(this.val$to::apply);
   }

   @Override
   public double toSliderValue(final R value) {
      return this.this$0.toSliderValue(this.val$from.applyAsInt(value));
   }

   @Override
   public Optional<R> next(final R current) {
      if (!this.val$discrete) {
         return Optional.empty();
      } else {
         int currentIndex = this.val$from.applyAsInt(current);
         return (Optional<R>)Optional.of(this.val$to.apply(this.this$0.validateValue(currentIndex + 1).orElse(currentIndex)));
      }
   }

   @Override
   public Optional<R> previous(final R current) {
      if (!this.val$discrete) {
         return Optional.empty();
      } else {
         int currentIndex = this.val$from.applyAsInt(current);
         return (Optional<R>)Optional.of(this.val$to.apply(this.this$0.validateValue(currentIndex - 1).orElse(currentIndex)));
      }
   }

   @Override
   public R fromSliderValue(final double slider) {
      return (R)this.val$to.apply(this.this$0.fromSliderValue(slider));
   }

   @Override
   public Codec<R> codec() {
      return this.this$0.codec().xmap(this.val$to::apply, this.val$from::applyAsInt);
   }
}
