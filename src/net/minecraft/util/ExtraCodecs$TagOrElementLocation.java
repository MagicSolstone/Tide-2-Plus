package net.minecraft.util;

import net.minecraft.resources.Identifier;

public record ExtraCodecs$TagOrElementLocation(Identifier id, boolean tag) {
   @Override
   public String toString() {
      return this.decoratedId();
   }

   private String decoratedId() {
      return this.tag ? "#" + this.id : this.id.toString();
   }
}
