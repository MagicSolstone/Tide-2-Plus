package net.minecraft.world.entity;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum Display$TextDisplay$Align implements StringRepresentable {
   CENTER("center"),
   LEFT("left"),
   RIGHT("right");

   public static final Codec<Display$TextDisplay$Align> CODEC = StringRepresentable.fromEnum(Display$TextDisplay$Align::values);
   private final String name;

   private Display$TextDisplay$Align(final String name) {
      this.name = name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
