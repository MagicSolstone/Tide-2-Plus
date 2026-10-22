package net.minecraft.client.renderer.block.dispatch;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface VariantMutator$VariantProperty<T> {
   Variant apply(Variant input, T value);

   default VariantMutator withValue(final T value) {
      return variant -> this.apply(variant, value);
   }
}
