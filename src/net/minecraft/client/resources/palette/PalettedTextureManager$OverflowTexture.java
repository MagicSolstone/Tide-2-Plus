package net.minecraft.client.resources.palette;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
class PalettedTextureManager$OverflowTexture extends DynamicTexture {
   private final Identifier location;

   public PalettedTextureManager$OverflowTexture(final Identifier location, final int width, final int height) {
      super(location::toString, width, height, false);
      this.location = location;
   }
}
