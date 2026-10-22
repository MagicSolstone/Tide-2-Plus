package net.minecraft.world.level.block;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum HangingSignBlock$Attachment implements StringRepresentable {
   WALL("wall"),
   CEILING("ceiling"),
   CEILING_MIDDLE("ceiling_middle");

   public static final Codec<HangingSignBlock$Attachment> CODEC = StringRepresentable.fromEnum(HangingSignBlock$Attachment::values);
   private final String name;

   private HangingSignBlock$Attachment(final String name) {
      this.name = name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
