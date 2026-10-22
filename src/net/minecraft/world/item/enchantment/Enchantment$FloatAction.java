package net.minecraft.world.item.enchantment;

import org.apache.commons.lang3.mutable.MutableFloat;

@FunctionalInterface
interface Enchantment$FloatAction<T> {
   float apply(T effect, float value);

   default Enchantment$GenericAction<T> asGeneric(final MutableFloat v) {
      return effect -> v.setValue(this.apply(effect, v.floatValue()));
   }
}
