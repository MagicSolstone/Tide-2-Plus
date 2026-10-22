package net.minecraft.client.renderer.special;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.StringRepresentable;

@Environment(EnvType.CLIENT)
public enum EndCubeSpecialRenderer$Type implements StringRepresentable {
   PORTAL("portal"),
   GATEWAY("gateway");

   public static final Codec<EndCubeSpecialRenderer$Type> CODEC = StringRepresentable.fromEnum(EndCubeSpecialRenderer$Type::values);
   private final String name;

   private EndCubeSpecialRenderer$Type(final String name) {
      this.name = name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
