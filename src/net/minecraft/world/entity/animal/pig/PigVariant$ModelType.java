package net.minecraft.world.entity.animal.pig;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum PigVariant$ModelType implements StringRepresentable {
   NORMAL("normal"),
   COLD("cold");

   public static final Codec<PigVariant$ModelType> CODEC = StringRepresentable.fromEnum(PigVariant$ModelType::values);
   private final String name;

   private PigVariant$ModelType(final String name) {
      this.name = name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
