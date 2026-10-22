package net.minecraft.world.level.biome;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;

public enum Biome$TemperatureModifier implements StringRepresentable {
   NONE("none"),
   FROZEN("frozen");

   private final String name;
   public static final Codec<Biome$TemperatureModifier> CODEC = StringRepresentable.fromEnum(Biome$TemperatureModifier::values);

   public abstract float modifyTemperature(final BlockPos pos, final float baseTemperature);

   private Biome$TemperatureModifier(final String name) {
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
