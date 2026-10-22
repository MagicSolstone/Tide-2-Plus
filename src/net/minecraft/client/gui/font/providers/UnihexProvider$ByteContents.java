package net.minecraft.client.gui.font.providers;

import it.unimi.dsi.fastutil.bytes.ByteList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record UnihexProvider$ByteContents(byte[] contents) implements UnihexProvider$LineData {
   @Override
   public int line(final int index) {
      return this.contents[index] << 24;
   }

   private static UnihexProvider$LineData read(final int line, final ByteList input) {
      byte[] content = new byte[16];
      int pos = 0;

      for (int i = 0; i < 16; i++) {
         int n1 = UnihexProvider.decodeHex(line, input, pos++);
         int n0 = UnihexProvider.decodeHex(line, input, pos++);
         byte v = (byte)(n1 << 4 | n0);
         content[i] = v;
      }

      return new UnihexProvider$ByteContents(content);
   }

   @Override
   public int bitWidth() {
      return 8;
   }
}
