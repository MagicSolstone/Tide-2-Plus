package net.minecraft.world.level.chunk;

import net.minecraft.core.IdMap;
import net.minecraft.util.Mth;

public abstract class Strategy<T> {
   private static final Palette$Factory SINGLE_VALUE_PALETTE_FACTORY = SingleValuePalette::create;
   private static final Palette$Factory LINEAR_PALETTE_FACTORY = LinearPalette::create;
   private static final Palette$Factory HASHMAP_PALETTE_FACTORY = HashMapPalette::create;
   private static final Configuration ZERO_BITS = new Configuration$Simple(SINGLE_VALUE_PALETTE_FACTORY, 0);
   private static final Configuration ONE_BIT_LINEAR = new Configuration$Simple(LINEAR_PALETTE_FACTORY, 1);
   private static final Configuration TWO_BITS_LINEAR = new Configuration$Simple(LINEAR_PALETTE_FACTORY, 2);
   private static final Configuration THREE_BITS_LINEAR = new Configuration$Simple(LINEAR_PALETTE_FACTORY, 3);
   private static final Configuration FOUR_BITS_LINEAR = new Configuration$Simple(LINEAR_PALETTE_FACTORY, 4);
   private static final Configuration FIVE_BITS_HASHMAP = new Configuration$Simple(HASHMAP_PALETTE_FACTORY, 5);
   private static final Configuration SIX_BITS_HASHMAP = new Configuration$Simple(HASHMAP_PALETTE_FACTORY, 6);
   private static final Configuration SEVEN_BITS_HASHMAP = new Configuration$Simple(HASHMAP_PALETTE_FACTORY, 7);
   private static final Configuration EIGHT_BITS_HASHMAP = new Configuration$Simple(HASHMAP_PALETTE_FACTORY, 8);
   private final IdMap<T> globalMap;
   private final GlobalPalette<T> globalPalette;
   protected final int globalPaletteBitsInMemory;
   private final int bitsPerAxis;
   private final int entryCount;

   private Strategy(final IdMap<T> globalMap, final int bitsPerAxis) {
      this.globalMap = globalMap;
      this.globalPalette = new GlobalPalette<>(globalMap);
      this.globalPaletteBitsInMemory = minimumBitsRequiredForDistinctValues(globalMap.size());
      this.bitsPerAxis = bitsPerAxis;
      this.entryCount = 1 << bitsPerAxis * 3;
   }

   public static <T> Strategy<T> createForBlockStates(final IdMap<T> registry) {
      return new Strategy$1(registry, 4);
   }

   public static <T> Strategy<T> createForBiomes(final IdMap<T> registry) {
      return new Strategy$2(registry, 2);
   }

   public int entryCount() {
      return this.entryCount;
   }

   public int getIndex(final int x, final int y, final int z) {
      return (y << this.bitsPerAxis | z) << this.bitsPerAxis | x;
   }

   public IdMap<T> globalMap() {
      return this.globalMap;
   }

   public GlobalPalette<T> globalPalette() {
      return this.globalPalette;
   }

   protected abstract Configuration getConfigurationForBitCount(int entryBits);

   protected Configuration getConfigurationForPaletteSize(final int paletteSize) {
      int bits = minimumBitsRequiredForDistinctValues(paletteSize);
      return this.getConfigurationForBitCount(bits);
   }

   private static int minimumBitsRequiredForDistinctValues(final int count) {
      return Mth.ceillog2(count);
   }
}
