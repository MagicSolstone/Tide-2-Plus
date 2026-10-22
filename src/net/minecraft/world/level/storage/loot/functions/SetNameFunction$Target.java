package net.minecraft.world.level.storage.loot.functions;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

public enum SetNameFunction$Target implements StringRepresentable {
   CUSTOM_NAME("custom_name"),
   ITEM_NAME("item_name");

   public static final Codec<SetNameFunction$Target> CODEC = StringRepresentable.fromEnum(SetNameFunction$Target::values);
   private final String name;

   private SetNameFunction$Target(final String name) {
      this.name = name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }

   public DataComponentType<Component> component() {
      return switch (this) {
         case CUSTOM_NAME -> DataComponents.CUSTOM_NAME;
         case ITEM_NAME -> DataComponents.ITEM_NAME;
      };
   }
}
