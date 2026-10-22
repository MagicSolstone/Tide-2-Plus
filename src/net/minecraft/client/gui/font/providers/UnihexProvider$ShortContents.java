package net.minecraft.client.gui.font.providers;

import it.unimi.dsi.fastutil.bytes.ByteList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record UnihexProvider$ShortContents(short[] contents) implements UnihexProvider$LineData {
   @Override
   public int line(final int index) {
      return this.contents[index] << 16;
   }

   private static UnihexProvider$LineData read(final int line, final ByteList input) {
      short[] content = new short[16];
      int pos = 0;

      for (int i = 0; i < 16; i++) {
         int n3 = UnihexProvider.decodeHex(line, input, pos++);
         int n2 = UnihexProvider.decodeHex(line, input, pos++);
         int n1 = UnihexProvider.decodeHex(line, input, pos++);
         int n0 = UnihexProvider.decodeHex(line, input, pos++);
         short v = (short)(n3 << 12 | n2 << 8 | n1 << 4 | n0);
         content[i] = v;
      }

      return new UnihexProvider$ShortContents(content);
   }

   @Override
   public int bitWidth() {
      return 16;
   }
}
