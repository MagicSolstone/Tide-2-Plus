package net.minecraft.nbt;

class ByteTag$Cache {
   private static final ByteTag[] cache = new ByteTag[256];

   private ByteTag$Cache() {
   }

   static {
      for (int i = 0; i < cache.length; i++) {
         cache[i] = new ByteTag((byte)(i - 128));
      }
   }
}
