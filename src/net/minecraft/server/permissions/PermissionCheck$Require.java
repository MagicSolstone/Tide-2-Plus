package net.minecraft.server.permissions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record PermissionCheck$Require(Permission permission) implements PermissionCheck {
   public static final MapCodec<PermissionCheck$Require> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(Permission.CODEC.fieldOf("permission").forGetter(PermissionCheck$Require::permission)).apply(i, PermissionCheck$Require::new)
   );

   @Override
   public MapCodec<PermissionCheck$Require> codec() {
      return MAP_CODEC;
   }

   @Override
   public boolean check(final PermissionSet source) {
      return source.hasPermission(this.permission);
   }
}
