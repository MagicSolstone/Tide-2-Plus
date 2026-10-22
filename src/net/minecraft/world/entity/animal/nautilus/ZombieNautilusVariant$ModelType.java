package net.minecraft.world.entity.animal.nautilus;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum ZombieNautilusVariant$ModelType implements StringRepresentable {
   NORMAL("normal"),
   WARM("warm");

   public static final Codec<ZombieNautilusVariant$ModelType> CODEC = StringRepresentable.fromEnum(ZombieNautilusVariant$ModelType::values);
   private final String name;

   private ZombieNautilusVariant$ModelType(final String name) {
      this.name = name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
