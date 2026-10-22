package net.minecraft.client.resources.model;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.StringRepresentable;

@Environment(EnvType.CLIENT)
public enum EquipmentClientInfo$LayerType implements StringRepresentable {
   HUMANOID("humanoid"),
   HUMANOID_LEGGINGS("humanoid_leggings"),
   HUMANOID_BABY("humanoid_baby"),
   WINGS("wings"),
   WOLF_BODY("wolf_body"),
   HORSE_BODY("horse_body"),
   LLAMA_BODY("llama_body"),
   PIG_SADDLE("pig_saddle"),
   STRIDER_SADDLE("strider_saddle"),
   CAMEL_SADDLE("camel_saddle"),
   CAMEL_HUSK_SADDLE("camel_husk_saddle"),
   HORSE_SADDLE("horse_saddle"),
   DONKEY_SADDLE("donkey_saddle"),
   MULE_SADDLE("mule_saddle"),
   ZOMBIE_HORSE_SADDLE("zombie_horse_saddle"),
   SKELETON_HORSE_SADDLE("skeleton_horse_saddle"),
   HAPPY_GHAST_BODY("happy_ghast_body"),
   NAUTILUS_SADDLE("nautilus_saddle"),
   NAUTILUS_BODY("nautilus_body");

   public static final Codec<EquipmentClientInfo$LayerType> CODEC = StringRepresentable.fromEnum(EquipmentClientInfo$LayerType::values);
   private final String id;

   private EquipmentClientInfo$LayerType(final String id) {
      this.id = id;
   }

   @Override
   public String getSerializedName() {
      return this.id;
   }

   public String trimAssetPrefix() {
      return "trims/entity/" + this.id;
   }
}
