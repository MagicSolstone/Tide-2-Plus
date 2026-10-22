package net.minecraft.server.permissions;

import com.mojang.serialization.MapCodec;

public class PermissionCheck$AlwaysPass implements PermissionCheck {
   public static final PermissionCheck$AlwaysPass INSTANCE = new PermissionCheck$AlwaysPass();
   public static final MapCodec<PermissionCheck$AlwaysPass> MAP_CODEC = MapCodec.unit(INSTANCE);

   private PermissionCheck$AlwaysPass() {
   }

   @Override
   public boolean check(final PermissionSet source) {
      return true;
   }

   @Override
   public MapCodec<PermissionCheck$AlwaysPass> codec() {
      return MAP_CODEC;
   }
}
