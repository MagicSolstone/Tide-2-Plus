package net.minecraft.world.item;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum CrossbowItem$ChargeType implements StringRepresentable {
   NONE("none"),
   ARROW("arrow"),
   ROCKET("rocket");

   public static final Codec<CrossbowItem$ChargeType> CODEC = StringRepresentable.fromEnum(CrossbowItem$ChargeType::values);
   private final String name;

   private CrossbowItem$ChargeType(final String name) {
      this.name = name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
