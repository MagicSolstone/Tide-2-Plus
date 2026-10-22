package net.minecraft.client;

import com.mojang.serialization.Codec;
import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.CycleButton$ValueListSupplier;

@Environment(EnvType.CLIENT)
public record OptionInstance$AltEnum<T>(
   List<T> values, List<T> altValues, BooleanSupplier altCondition, OptionInstance$CycleableValueSet$ValueSetter<T> valueSetter, Codec<T> codec
) implements OptionInstance$CycleableValueSet<T> {
   @Override
   public CycleButton$ValueListSupplier<T> valueListSupplier() {
      return CycleButton$ValueListSupplier.create(this.altCondition, this.values, this.altValues);
   }

   @Override
   public Optional<T> validateValue(final T value) {
      return (this.altCondition.getAsBoolean() ? this.altValues : this.values).contains(value) ? Optional.of(value) : Optional.empty();
   }
}
