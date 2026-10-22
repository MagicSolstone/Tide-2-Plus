package net.minecraft.client.gui.font.providers;

import it.unimi.dsi.fastutil.bytes.ByteList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record UnihexProvider$IntContents(int[] contents, int bitWidth) implements UnihexProvider$LineData {
   private static final int SIZE_24 = 24;

   @Override
   public int line(final int index) {
      return this.contents[index];
   }

   private static UnihexProvider$LineData read24(final int line, final ByteList input) {
      int[] content = new int[16];
      int mask = 0;
      int pos = 0;

      for (int i = 0; i < 16; i++) {
         int n5 = UnihexProvider.decodeHex(line, input, pos++);
         int n4 = UnihexProvider.decodeHex(line, input, pos++);
         int n3 = UnihexProvider.decodeHex(line, input, pos++);
         int n2 = UnihexProvider.decodeHex(line, input, pos++);
         int n1 = UnihexProvider.decodeHex(line, input, pos++);
         int n0 = UnihexProvider.decodeHex(line, input, pos++);
         int v = n5 << 20 | n4 << 16 | n3 << 12 | n2 << 8 | n1 << 4 | n0;
         content[i] = v << 8;
         mask |= v;
      }

      return new UnihexProvider$IntContents(content, 24);
   }

   public static UnihexProvider$LineData read32(final int line, final ByteList input) {
      int[] content = new int[16];
      int mask = 0;
      int pos = 0;

      for (int i = 0; i < 16; i++) {
         int n7 = UnihexProvider.decodeHex(line, input, pos++);
         int n6 = UnihexProvider.decodeHex(line, input, pos++);
         int n5 = UnihexProvider.decodeHex(line, input, pos++);
         int n4 = UnihexProvider.decodeHex(line, input, pos++);
         int n3 = UnihexProvider.decodeHex(line, input, pos++);
         int n2 = UnihexProvider.decodeHex(line, input, pos++);
         int n1 = UnihexProvider.decodeHex(line, input, pos++);
         int n0 = UnihexProvider.decodeHex(line, input, pos++);
         int v = n7 << 28 | n6 << 24 | n5 << 20 | n4 << 16 | n3 << 12 | n2 << 8 | n1 << 4 | n0;
         content[i] = v;
         mask |= v;
      }

      return new UnihexProvider$IntContents(content, 32);
   }
}
