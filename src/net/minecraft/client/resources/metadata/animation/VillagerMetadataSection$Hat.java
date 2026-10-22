package net.minecraft.client.resources.metadata.animation;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.StringRepresentable;

@Environment(EnvType.CLIENT)
public enum VillagerMetadataSection$Hat implements StringRepresentable {
   NONE("none"),
   PARTIAL("partial"),
   FULL("full");

   public static final Codec<VillagerMetadataSection$Hat> CODEC = StringRepresentable.fromEnum(VillagerMetadataSection$Hat::values);
   private final String name;

   private VillagerMetadataSection$Hat(final String name) {
      this.name = name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
