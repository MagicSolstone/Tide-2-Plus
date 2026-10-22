package net.minecraft.server.permissions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Permission$HasCommandLevel(PermissionLevel level) implements Permission {
   public static final MapCodec<Permission$HasCommandLevel> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(PermissionLevel.CODEC.fieldOf("level").forGetter(Permission$HasCommandLevel::level)).apply(i, Permission$HasCommandLevel::new)
   );

   @Override
   public MapCodec<Permission$HasCommandLevel> codec() {
      return MAP_CODEC;
   }
}
