package net.minecraft.world.level.block.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.StringRepresentable$EnumCodec;

public enum JigsawBlockEntity$JointType implements StringRepresentable {
   ROLLABLE("rollable"),
   ALIGNED("aligned");

   public static final StringRepresentable$EnumCodec<JigsawBlockEntity$JointType> CODEC = StringRepresentable.fromEnum(JigsawBlockEntity$JointType::values);
   private final String name;

   private JigsawBlockEntity$JointType(final String name) {
      this.name = name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }

   public Component getTranslatedName() {
      return Component.translatable("jigsaw_block.joint." + this.name);
   }
}
