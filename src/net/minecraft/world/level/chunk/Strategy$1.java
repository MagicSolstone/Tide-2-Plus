package net.minecraft.world.level.chunk;

import net.minecraft.core.IdMap;

class Strategy$1 extends Strategy<T> {
   Strategy$1(final IdMap globalMap, final int bitsPerAxis) {
      super(globalMap, bitsPerAxis);
   }

   @Override
   public Configuration getConfigurationForBitCount(final int entryBits) {
      return (Configuration)(switch (entryBits) {
         case 0 -> Strategy.ZERO_BITS;
         case 1, 2, 3, 4 -> Strategy.FOUR_BITS_LINEAR;
         case 5 -> Strategy.FIVE_BITS_HASHMAP;
         case 6 -> Strategy.SIX_BITS_HASHMAP;
         case 7 -> Strategy.SEVEN_BITS_HASHMAP;
         case 8 -> Strategy.EIGHT_BITS_HASHMAP;
         default -> new Configuration$Global(this.globalPaletteBitsInMemory, entryBits);
      });
   }
}
