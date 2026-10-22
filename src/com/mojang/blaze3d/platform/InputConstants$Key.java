package com.mojang.blaze3d.platform;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public final class InputConstants$Key {
   private final String name;
   private final InputConstants$Type type;
   private final int value;
   private static final Map<String, InputConstants$Key> NAME_MAP = Maps.newHashMap();

   private InputConstants$Key(final String name, final InputConstants$Type type, final int value) {
      this.name = name;
      this.type = type;
      this.value = value;
      NAME_MAP.put(name, this);
   }

   public InputConstants$Type getType() {
      return this.type;
   }

   public int getValue() {
      return this.value;
   }

   public String getName() {
      return this.name;
   }

   public Component getDisplayName() {
      return this.type.displayTextSupplier.apply(this.value, this.name);
   }

   public OptionalInt getNumericKeyValue() {
      return switch (this.value) {
         case 30, 89 -> OptionalInt.of(1);
         case 31, 90 -> OptionalInt.of(2);
         case 32, 91 -> OptionalInt.of(3);
         case 33, 92 -> OptionalInt.of(4);
         case 34, 93 -> OptionalInt.of(5);
         case 35, 94 -> OptionalInt.of(6);
         case 36, 95 -> OptionalInt.of(7);
         case 37, 96 -> OptionalInt.of(8);
         case 38, 97 -> OptionalInt.of(9);
         case 39, 98 -> OptionalInt.of(0);
         default -> OptionalInt.empty();
      };
   }

   @Override
   public boolean equals(final Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         InputConstants$Key key = (InputConstants$Key)o;
         return this.value == key.value && this.type == key.type;
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return Objects.hash(this.type, this.value);
   }

   @Override
   public String toString() {
      return this.name;
   }
}
