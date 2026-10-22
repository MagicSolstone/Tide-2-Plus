package net.minecraft.world.level.biome;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum Biome$Precipitation implements StringRepresentable {
   NONE("none"),
   RAIN("rain"),
   SNOW("snow");

   public static final Codec<Biome$Precipitation> CODEC = StringRepresentable.fromEnum(Biome$Precipitation::values);
   private final String name;

   private Biome$Precipitation(final String name) {
      this.name = name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
