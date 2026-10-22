package net.minecraft.world.level.lighting;

public class LayerLightSectionStorage$SectionState {
   public static final byte EMPTY = 0;
   private static final int MIN_NEIGHBORS = 0;
   private static final int MAX_NEIGHBORS = 26;
   private static final byte HAS_DATA_BIT = 32;
   private static final byte NEIGHBOR_COUNT_BITS = 31;

   protected LayerLightSectionStorage$SectionState() {
   }

   public static byte hasData(final byte state, final boolean hasData) {
      return (byte)(hasData ? state | 32 : state & -33);
   }

   public static byte neighborCount(final byte state, final int neighborCount) {
      if (neighborCount >= 0 && neighborCount <= 26) {
         return (byte)(state & -32 | neighborCount & 31);
      } else {
         throw new IllegalArgumentException("Neighbor count was not within range [0; 26]");
      }
   }

   public static boolean hasData(final byte state) {
      return (state & 32) != 0;
   }

   public static int neighborCount(final byte state) {
      return state & 31;
   }

   public static LayerLightSectionStorage$SectionType type(final byte state) {
      if (state == 0) {
         return LayerLightSectionStorage$SectionType.EMPTY;
      } else {
         return hasData(state) ? LayerLightSectionStorage$SectionType.LIGHT_AND_DATA : LayerLightSectionStorage$SectionType.LIGHT_ONLY;
      }
   }
}
