package net.minecraft.world.level.biome;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum BiomeSpecialEffects$GrassColorModifier implements StringRepresentable {
   NONE("none"),
   DARK_FOREST("dark_forest"),
   SWAMP("swamp");

   private final String name;
   public static final Codec<BiomeSpecialEffects$GrassColorModifier> CODEC = StringRepresentable.fromEnum(BiomeSpecialEffects$GrassColorModifier::values);

   public abstract int modifyColor(final double x, final double z, final int baseColor);

   private BiomeSpecialEffects$GrassColorModifier(final String name) {
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
