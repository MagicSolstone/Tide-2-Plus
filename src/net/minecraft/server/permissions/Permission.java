package net.minecraft.server.permissions;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public interface Permission {
   Codec<Permission> FULL_CODEC = BuiltInRegistries.PERMISSION_TYPE.byNameCodec().dispatch(Permission::codec, c -> c);
   Codec<Permission> CODEC = Codec.either(FULL_CODEC, Identifier.CODEC)
      .xmap(
         e -> (Permission)e.map(permission -> permission, Permission$Atom::create),
         permission -> permission instanceof Permission$Atom atom ? Either.right(atom.id()) : Either.left(permission)
      );

   MapCodec<? extends Permission> codec();
}
