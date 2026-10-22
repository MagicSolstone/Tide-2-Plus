package com.mojang.blaze3d.font;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.FontOption$Filter;

@Environment(EnvType.CLIENT)
public record GlyphProvider$Conditional(GlyphProvider provider, FontOption$Filter filter) implements AutoCloseable {
   @Override
   public void close() {
      this.provider.close();
   }
}
