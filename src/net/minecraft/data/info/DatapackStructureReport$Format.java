package net.minecraft.data.info;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

enum DatapackStructureReport$Format implements StringRepresentable {
   STRUCTURE("structure"),
   MCFUNCTION("mcfunction");

   public static final Codec<DatapackStructureReport$Format> CODEC = StringRepresentable.fromEnum(DatapackStructureReport$Format::values);
   private final String name;

   private DatapackStructureReport$Format(final String name) {
      this.name = name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
