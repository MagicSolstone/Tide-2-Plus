package net.minecraft.client.gui.components;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.List;
import java.util.function.BooleanSupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface CycleButton$ValueListSupplier<T> {
   List<T> getSelectedList();

   List<T> getDefaultList();

   static <T> CycleButton$ValueListSupplier<T> create(final Collection<T> values) {
      List<T> copy = ImmutableList.copyOf(values);
      return new CycleButton$ValueListSupplier$1(copy);
   }

   static <T> CycleButton$ValueListSupplier<T> create(final BooleanSupplier altSelector, final List<T> defaultList, final List<T> altList) {
      List<T> defaultCopy = ImmutableList.copyOf(defaultList);
      List<T> altCopy = ImmutableList.copyOf(altList);
      return new CycleButton$ValueListSupplier$2(altSelector, altCopy, defaultCopy);
   }
}
