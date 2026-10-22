package net.minecraft.client.renderer.rendertype;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum RenderSetup$OutlineProperty {
   NONE("none"),
   IS_OUTLINE("is_outline"),
   AFFECTS_OUTLINE("affects_outline");

   private final String name;

   private RenderSetup$OutlineProperty(final String name) {
      this.name = name;
   }

   @Override
   public String toString() {
      return this.name;
   }
}
