package net.minecraft.client.gui.font.providers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface UnihexProvider$LineData {
   int line(int index);

   int bitWidth();

   default int mask() {
      int mask = 0;

      for (int i = 0; i < 16; i++) {
         mask |= this.line(i);
      }

      return mask;
   }

   default int calculateWidth() {
      int mask = this.mask();
      int bitWidth = this.bitWidth();
      int left;
      int right;
      if (mask == 0) {
         left = 0;
         right = bitWidth;
      } else {
         left = Integer.numberOfLeadingZeros(mask);
         right = 32 - Integer.numberOfTrailingZeros(mask) - 1;
      }

      return UnihexProvider$Dimensions.pack(left, right);
   }
}
