package net.minecraft.client.resources.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum UnbakedModel$GuiLight {
   FRONT("front"),
   SIDE("side");

   private final String name;

   private UnbakedModel$GuiLight(final String name) {
      this.name = name;
   }

   public static UnbakedModel$GuiLight getByName(final String name) {
      for (UnbakedModel$GuiLight target : values()) {
         if (target.name.equals(name)) {
            return target;
         }
      }

      throw new IllegalArgumentException("Invalid gui light: " + name);
   }

   public boolean lightLikeBlock() {
      return this == SIDE;
   }
}
