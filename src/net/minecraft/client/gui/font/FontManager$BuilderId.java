package net.minecraft.client.gui.font;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
record FontManager$BuilderId(Identifier fontId, String pack, int index) {
   @Override
   public String toString() {
      return "(" + this.fontId + ": builder #" + this.index + " from pack " + this.pack + ")";
   }
}
