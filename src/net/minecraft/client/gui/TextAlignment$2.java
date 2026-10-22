package net.minecraft.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
enum TextAlignment$2 {
   @Override
   public int calculateLeft(final int anchor, final int width) {
      return anchor - width / 2;
   }
}
