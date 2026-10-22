package net.minecraft.world.level.material;

import com.google.common.base.Preconditions;

public enum MapColor$Brightness {
   LOW(0, 180),
   NORMAL(1, 220),
   HIGH(2, 255),
   LOWEST(3, 135);

   private static final MapColor$Brightness[] VALUES = new MapColor$Brightness[]{LOW, NORMAL, HIGH, LOWEST};
   public final int id;
   public final int modifier;

   private MapColor$Brightness(final int id, final int modifier) {
      this.id = id;
      this.modifier = modifier;
   }

   public static MapColor$Brightness byId(final int id) {
      Preconditions.checkPositionIndex(id, VALUES.length, "brightness id");
      return byIdUnsafe(id);
   }

   private static MapColor$Brightness byIdUnsafe(final int id) {
      return VALUES[id];
   }
}
