package net.minecraft.util.datafix.fixes;

class ChunkPalettedStorageFix$DataLayer {
   private static final int SIZE = 2048;
   private static final int NIBBLE_SIZE = 4;
   private final byte[] data;

   public ChunkPalettedStorageFix$DataLayer() {
      this.data = new byte[2048];
   }

   public ChunkPalettedStorageFix$DataLayer(final byte[] data) {
      this.data = data;
      if (data.length != 2048) {
         throw new IllegalArgumentException("ChunkNibbleArrays should be 2048 bytes not: " + data.length);
      }
   }

   public int get(final int x, final int y, final int z) {
      int position = this.getPosition(y << 8 | z << 4 | x);
      return this.isFirst(y << 8 | z << 4 | x) ? this.data[position] & 15 : this.data[position] >> 4 & 15;
   }

   private boolean isFirst(final int position) {
      return (position & 1) == 0;
   }

   private int getPosition(final int position) {
      return position >> 1;
   }
}
