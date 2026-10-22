package net.minecraft.world.entity.animal.chicken;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum ChickenVariant$ModelType implements StringRepresentable {
   NORMAL("normal"),
   COLD("cold");

   public static final Codec<ChickenVariant$ModelType> CODEC = StringRepresentable.fromEnum(ChickenVariant$ModelType::values);
   private final String name;

   private ChickenVariant$ModelType(final String name) {
      this.name = name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
