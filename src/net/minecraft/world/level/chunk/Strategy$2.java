package net.minecraft.world.level.chunk;

import net.minecraft.core.IdMap;

class Strategy$2 extends Strategy<T> {
   Strategy$2(final IdMap globalMap, final int bitsPerAxis) {
      super(globalMap, bitsPerAxis);
   }

   @Override
   public Configuration getConfigurationForBitCount(final int entryBits) {
      return (Configuration)(switch (entryBits) {
         case 0 -> Strategy.ZERO_BITS;
         case 1 -> Strategy.ONE_BIT_LINEAR;
         case 2 -> Strategy.TWO_BITS_LINEAR;
         case 3 -> Strategy.THREE_BITS_LINEAR;
         default -> new Configuration$Global(this.globalPaletteBitsInMemory, entryBits);
      });
   }
}
