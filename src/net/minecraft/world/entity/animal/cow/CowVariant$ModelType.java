package net.minecraft.world.entity.animal.cow;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum CowVariant$ModelType implements StringRepresentable {
   NORMAL("normal"),
   COLD("cold"),
   WARM("warm");

   public static final Codec<CowVariant$ModelType> CODEC = StringRepresentable.fromEnum(CowVariant$ModelType::values);
   private final String name;

   private CowVariant$ModelType(final String name) {
      this.name = name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
