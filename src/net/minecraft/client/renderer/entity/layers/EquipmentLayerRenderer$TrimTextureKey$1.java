package net.minecraft.client.renderer.entity.layers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.palette.PalettedTextureManager$Handle;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
class EquipmentLayerRenderer$TrimTextureKey$1 implements PalettedTextureManager$Handle {
   EquipmentLayerRenderer$TrimTextureKey$1(final Identifier val$textureLocation) {
      this.val$textureLocation = val$textureLocationx;
   }

   @Override
   public Identifier textureLocation() {
      return this.val$textureLocation;
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
