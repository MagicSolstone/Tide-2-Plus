package net.minecraft.client.resources.palette;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
class PalettedTextureManager$2 implements PalettedTextureManager$Handle {
   PalettedTextureManager$2(final PalettedTextureManager this$0) {
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public Identifier textureLocation() {
      return MissingTextureAtlasSprite.getLocation();
   }

   @Override
   public float getU(final float offset) {
      return offset;
   }

   @Override
   public float getV(final float offset) {
      return offset;
   }
}
