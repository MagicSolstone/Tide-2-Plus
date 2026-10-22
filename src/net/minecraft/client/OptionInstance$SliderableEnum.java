package net.minecraft.client;

import com.mojang.serialization.Codec;
import java.util.List;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public record OptionInstance$SliderableEnum<T>(List<T> values, Codec<T> codec) implements OptionInstance$SliderableValueSet<T> {
   @Override
   public double toSliderValue(final T value) {
      if (value == this.values.getFirst()) {
         return 0.0;
      } else {
         return value == this.values.getLast() ? 1.0 : Mth.map((double)this.values.indexOf(value), 0.0, (double)(this.values.size() - 1), 0.0, 1.0);
      }
   }

   @Override
   public Optional<T> next(final T current) {
      int currentIntex = this.values.indexOf(current);
      int nextIndex = Mth.clamp(currentIntex + 1, 0, this.values.size() - 1);
      return Optional.of(this.values.get(nextIndex));
   }

   @Override
   public Optional<T> previous(final T current) {
      int currentIntex = this.values.indexOf(current);
      int previousIndex = Mth.clamp(currentIntex - 1, 0, this.values.size() - 1);
      return Optional.of(this.values.get(previousIndex));
   }

   @Override
   public T fromSliderValue(double slider) {
      if (slider >= 1.0) {
         slider = 0.99999F;
      }

      int index = Mth.floor(Mth.map(slider, 0.0, 1.0, 0.0, (double)this.values.size()));
      return this.values.get(Mth.clamp(index, 0, this.values.size() - 1));
   }

   @Override
   public Optional<T> validateValue(final T value) {
      int index = this.values.indexOf(value);
      return index > -1 ? Optional.of(value) : Optional.empty();
   }
}
