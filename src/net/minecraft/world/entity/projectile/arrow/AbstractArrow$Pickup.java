package net.minecraft.world.entity.projectile.arrow;

import com.mojang.serialization.Codec;

public enum AbstractArrow$Pickup {
   DISALLOWED,
   ALLOWED,
   CREATIVE_ONLY;

   public static final Codec<AbstractArrow$Pickup> LEGACY_CODEC = Codec.BYTE.xmap(AbstractArrow$Pickup::byOrdinal, p -> (byte)p.ordinal());

   public static AbstractArrow$Pickup byOrdinal(int ordinal) {
      if (ordinal < 0 || ordinal > values().length) {
         ordinal = 0;
      }

      return values()[ordinal];
   }
}
