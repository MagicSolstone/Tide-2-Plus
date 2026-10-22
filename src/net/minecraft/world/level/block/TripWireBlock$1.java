package net.minecraft.world.level.block;

// $VF: synthetic class
class TripWireBlock$1 {
   static {
      try {
         $SwitchMap$net$minecraft$world$level$block$Mirror[Mirror.LEFT_RIGHT.ordinal()] = 1;
      } catch (NoSuchFieldError var5) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$Mirror[Mirror.FRONT_BACK.ordinal()] = 2;
      } catch (NoSuchFieldError var4) {
      }

      $SwitchMap$net$minecraft$world$level$block$Rotation = new int[Rotation.values().length];

      try {
         $SwitchMap$net$minecraft$world$level$block$Rotation[Rotation.CLOCKWISE_180.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$Rotation[Rotation.COUNTERCLOCKWISE_90.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$Rotation[Rotation.CLOCKWISE_90.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
