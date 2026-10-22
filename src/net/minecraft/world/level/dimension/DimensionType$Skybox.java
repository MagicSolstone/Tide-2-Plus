package net.minecraft.world.level.dimension;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum DimensionType$Skybox implements StringRepresentable {
   NONE("none"),
   OVERWORLD("overworld"),
   END("end");

   public static final Codec<DimensionType$Skybox> CODEC = StringRepresentable.fromEnum(DimensionType$Skybox::values);
   private final String name;

   private DimensionType$Skybox(final String name) {
      this.name = name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
