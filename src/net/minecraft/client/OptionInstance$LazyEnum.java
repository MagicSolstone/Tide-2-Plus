package net.minecraft.client;

import com.mojang.serialization.Codec;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.CycleButton$ValueListSupplier;

@Environment(EnvType.CLIENT)
public record OptionInstance$LazyEnum<T>(Supplier<List<T>> values, Function<T, Optional<T>> validateValue, Codec<T> codec)
   implements OptionInstance$CycleableValueSet<T> {
   @Override
   public Optional<T> validateValue(final T value) {
      return this.validateValue.apply(value);
   }

   @Override
   public CycleButton$ValueListSupplier<T> valueListSupplier() {
      return CycleButton$ValueListSupplier.create(this.values.get());
   }
}
