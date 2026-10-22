package net.minecraft.world.level.levelgen.structure.structures;

import com.mojang.serialization.Codec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;

public enum OceanRuinStructure$Type implements StringRepresentable {
   WARM("warm"),
   COLD("cold");

   public static final Codec<OceanRuinStructure$Type> CODEC = StringRepresentable.fromEnum(OceanRuinStructure$Type::values);
   @Deprecated
   public static final Codec<OceanRuinStructure$Type> LEGACY_CODEC = ExtraCodecs.legacyEnum(OceanRuinStructure$Type::valueOf);
   private final String name;

   private OceanRuinStructure$Type(final String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
