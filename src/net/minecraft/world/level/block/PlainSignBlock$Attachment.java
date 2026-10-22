package net.minecraft.world.level.block;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum PlainSignBlock$Attachment implements StringRepresentable {
   WALL("wall"),
   GROUND("ground");

   public static final Codec<PlainSignBlock$Attachment> CODEC = StringRepresentable.fromEnum(PlainSignBlock$Attachment::values);
   private final String name;

   private PlainSignBlock$Attachment(final String name) {
      this.name = name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
