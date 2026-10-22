package net.minecraft.world.level;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum CardinalLighting$Type implements StringRepresentable {
   DEFAULT("default", CardinalLighting.DEFAULT),
   NETHER("nether", CardinalLighting.NETHER);

   public static final Codec<CardinalLighting$Type> CODEC = StringRepresentable.fromEnum(CardinalLighting$Type::values);
   private final String name;
   private final CardinalLighting lighting;

   private CardinalLighting$Type(final String name, final CardinalLighting lighting) {
      this.name = name;
      this.lighting = lighting;
   }

   public CardinalLighting get() {
      return this.lighting;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
