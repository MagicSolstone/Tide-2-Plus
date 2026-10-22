package net.minecraft.nbt;

class ShortTag$Cache {
   private static final int HIGH = 1024;
   private static final int LOW = -128;
   private static final ShortTag[] cache = new ShortTag[1153];

   private ShortTag$Cache() {
   }

   static {
      for (int i = 0; i < cache.length; i++) {
         cache[i] = new ShortTag((short)(-128 + i));
      }
   }
}
