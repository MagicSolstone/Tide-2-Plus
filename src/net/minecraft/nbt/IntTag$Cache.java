package net.minecraft.nbt;

class IntTag$Cache {
   private static final int HIGH = 1024;
   private static final int LOW = -128;
   private static final IntTag[] cache = new IntTag[1153];

   private IntTag$Cache() {
   }

   static {
      for (int i = 0; i < cache.length; i++) {
         cache[i] = new IntTag(-128 + i);
      }
   }
}
