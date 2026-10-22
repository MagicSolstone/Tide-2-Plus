package net.minecraft.client.resources.palette;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
record PalettedTextureManager$Slot(AbstractTexture texture, Identifier textureLocation, int x, int y, int width, int height)
   implements PalettedTextureManager$Handle {
   @Override
   public float getU(final float offset) {
      return (this.x + offset * this.width) / this.texture.getTexture().getWidth(0);
   }

   @Override
   public float getV(final float offset) {
      return (this.y + offset * this.height) / this.texture.getTexture().getHeight(0);
   }
}
