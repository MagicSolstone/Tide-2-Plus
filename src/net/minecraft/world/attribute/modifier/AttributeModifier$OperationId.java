package net.minecraft.world.attribute.modifier;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum AttributeModifier$OperationId implements StringRepresentable {
   OVERRIDE("override"),
   ALPHA_BLEND("alpha_blend"),
   ADD("add"),
   SUBTRACT("subtract"),
   MULTIPLY("multiply"),
   BLEND_TO_GRAY("blend_to_gray"),
   MINIMUM("minimum"),
   MAXIMUM("maximum"),
   AND("and"),
   NAND("nand"),
   OR("or"),
   NOR("nor"),
   XOR("xor"),
   XNOR("xnor"),
   APPEND("append"),
   OVERLAY("overlay");

   public static final Codec<AttributeModifier$OperationId> CODEC = StringRepresentable.fromEnum(AttributeModifier$OperationId::values);
   private final String name;

   private AttributeModifier$OperationId(final String name) {
      this.name = name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
