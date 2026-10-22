package net.minecraft.server.permissions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public record Permission$Atom(Identifier id) implements Permission {
   public static final MapCodec<Permission$Atom> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(Identifier.CODEC.fieldOf("id").forGetter(Permission$Atom::id)).apply(i, Permission$Atom::new)
   );

   @Override
   public MapCodec<Permission$Atom> codec() {
      return MAP_CODEC;
   }

   public static Permission$Atom create(final String name) {
      return create(Identifier.withDefaultNamespace(name));
   }

   public static Permission$Atom create(final Identifier id) {
      return new Permission$Atom(id);
   }
}
