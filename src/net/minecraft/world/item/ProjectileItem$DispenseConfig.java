package net.minecraft.world.item;

import java.util.Optional;

public record ProjectileItem$DispenseConfig(
   ProjectileItem$PositionFunction positionFunction, float uncertainty, float power, Optional<Integer> overrideDispenseEvent
) {
   public static final ProjectileItem$DispenseConfig DEFAULT = builder().build();

   public static ProjectileItem$DispenseConfig$Builder builder() {
      return new ProjectileItem$DispenseConfig$Builder();
   }
}
