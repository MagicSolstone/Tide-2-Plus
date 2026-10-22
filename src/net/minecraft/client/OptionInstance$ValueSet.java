package net.minecraft.client;

import com.mojang.serialization.Codec;
import java.util.Optional;
import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.AbstractWidget;

@Environment(EnvType.CLIENT)
public interface OptionInstance$ValueSet<T> {
   Function<OptionInstance<T>, AbstractWidget> createButton(
      final OptionInstance$TooltipSupplier<T> tooltip,
      Options options,
      final int x,
      final int y,
      final int width,
      final OptionInstance$ValueUpdateListener<? super T> onValueChanged
   );

   Optional<T> validateValue(final T value);

   Codec<T> codec();
}
