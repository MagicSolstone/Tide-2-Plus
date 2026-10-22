package net.minecraft.world.level.block;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum BannerBlock$AttachmentType implements StringRepresentable {
   WALL("wall"),
   GROUND("ground");

   public static final Codec<BannerBlock$AttachmentType> CODEC = StringRepresentable.fromEnum(BannerBlock$AttachmentType::values);
   private final String name;

   private BannerBlock$AttachmentType(final String name) {
      this.name = name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
