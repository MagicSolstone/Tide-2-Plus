package net.minecraft.client.gui.font;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface TextRenderable$Styled extends TextRenderable, ActiveArea {
   @Override
   default float activeLeft() {
      return this.left();
   }

   @Override
   default float activeTop() {
      return this.top();
   }

   @Override
   default float activeRight() {
      return this.right();
   }

   @Override
   default float activeBottom() {
      return this.bottom();
   }
}
