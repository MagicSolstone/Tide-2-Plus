package net.minecraft.nbt;

class LongTag$Cache {
   private static final int HIGH = 1024;
   private static final int LOW = -128;
   private static final LongTag[] cache = new LongTag[1153];

   private LongTag$Cache() {
   }

   static {
      for (int i = 0; i < cache.length; i++) {
         cache[i] = new LongTag(-128 + i);
      }
   }
}
