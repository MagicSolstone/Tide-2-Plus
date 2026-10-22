package net.minecraft.server.packs.repository;

import java.util.List;
import java.util.function.Function;
import net.minecraft.server.packs.PackSelectionConfig;

public enum Pack$Position {
   TOP,
   BOTTOM;

   public <T> int insert(final List<T> list, final T value, final Function<T, PackSelectionConfig> converter, final boolean reverse) {
      Pack$Position self = reverse ? this.opposite() : this;
      if (self == BOTTOM) {
         int index;
         for (index = 0; index < list.size(); index++) {
            PackSelectionConfig pack = converter.apply(list.get(index));
            if (!pack.fixedPosition() || pack.defaultPosition() != this) {
               break;
            }
         }

         list.add(index, value);
         return index;
      } else {
         int index;
         for (index = list.size() - 1; index >= 0; index--) {
            PackSelectionConfig pack = converter.apply(list.get(index));
            if (!pack.fixedPosition() || pack.defaultPosition() != this) {
               break;
            }
         }

         list.add(index + 1, value);
         return index + 1;
      }
   }

   public Pack$Position opposite() {
      return this == TOP ? BOTTOM : TOP;
   }
}
