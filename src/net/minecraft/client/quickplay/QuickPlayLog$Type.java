package net.minecraft.client.quickplay;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.StringRepresentable;

@Environment(EnvType.CLIENT)
public enum QuickPlayLog$Type implements StringRepresentable {
   SINGLEPLAYER("singleplayer"),
   MULTIPLAYER("multiplayer"),
   REALMS("realms");

   private static final Codec<QuickPlayLog$Type> CODEC = StringRepresentable.fromEnum(QuickPlayLog$Type::values);
   private final String name;

   private QuickPlayLog$Type(final String name) {
      this.name = name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
