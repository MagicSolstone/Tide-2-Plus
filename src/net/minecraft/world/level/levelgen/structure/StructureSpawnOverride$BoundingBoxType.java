package net.minecraft.world.level.levelgen.structure;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum StructureSpawnOverride$BoundingBoxType implements StringRepresentable {
   PIECE("piece"),
   STRUCTURE("full");

   public static final Codec<StructureSpawnOverride$BoundingBoxType> CODEC = StringRepresentable.fromEnum(StructureSpawnOverride$BoundingBoxType::values);
   private final String id;

   private StructureSpawnOverride$BoundingBoxType(final String id) {
      this.id = id;
   }

   @Override
   public String getSerializedName() {
      return this.id;
   }
}
