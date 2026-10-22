package net.minecraft.client;

import com.mojang.serialization.Codec;
import java.util.Objects;
import java.util.Optional;
import java.util.function.DoubleFunction;
import java.util.function.ToDoubleFunction;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class OptionInstance$UnitDouble$1 implements OptionInstance$SliderableValueSet<R> {
   OptionInstance$UnitDouble$1(final OptionInstance$UnitDouble this$0, final ToDoubleFunction val$from, final DoubleFunction val$to) {
      this.val$from = val$fromx;
      this.val$to = val$tox;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public Optional<R> validateValue(final R value) {
      return this.this$0.validateValue(this.val$from.applyAsDouble(value)).map(this.val$to::apply);
   }

   @Override
   public double toSliderValue(final R value) {
      return this.this$0.toSliderValue(this.val$from.applyAsDouble(value));
   }

   @Override
   public R fromSliderValue(final double slider) {
      return (R)this.val$to.apply(this.this$0.fromSliderValue(slider));
   }

   @Override
   public Codec<R> codec() {
      return this.this$0.codec().xmap(this.val$to::apply, this.val$from::applyAsDouble);
   }
}
