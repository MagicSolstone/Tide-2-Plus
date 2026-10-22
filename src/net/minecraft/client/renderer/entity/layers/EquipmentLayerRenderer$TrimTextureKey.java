package net.minecraft.client.renderer.entity.layers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.client.resources.model.EquipmentClientInfo$LayerType;
import net.minecraft.client.resources.model.EquipmentClientInfo$TrimOverride;
import net.minecraft.client.resources.palette.PalettedTextureManager;
import net.minecraft.client.resources.palette.PalettedTextureManager$Handle;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.equipment.trim.ArmorTrim;

@Environment(EnvType.CLIENT)
record EquipmentLayerRenderer$TrimTextureKey(ArmorTrim trim, EquipmentClientInfo$LayerType layerType, EquipmentClientInfo equipmentInfo) {
   private PalettedTextureManager$Handle getOrPrepareTexture(final PalettedTextureManager palettedTextures) {
      Identifier textureId = this.trim.pattern().value().assetId();
      Identifier paletteId = this.trim.material().value().paletteId();

      for (EquipmentClientInfo$TrimOverride override : this.equipmentInfo.trimOverrides()) {
         if (override.predicate().matches(this.trim)) {
            textureId = override.textureId().orElse(textureId);
            paletteId = override.paletteId().orElse(null);
            break;
         }
      }

      Identifier baseTexture = textureId.withPath(path -> this.layerType.trimAssetPrefix() + "/" + path);
      return paletteId == null ? createTextureWithNoPalette(baseTexture) : palettedTextures.getOrPrepare(baseTexture, paletteId);
   }

   private static PalettedTextureManager$Handle createTextureWithNoPalette(final Identifier texture) {
      Identifier textureLocation = texture.withPath(path -> "textures/" + path + ".png");
      return new EquipmentLayerRenderer$TrimTextureKey$1(textureLocation);
   }
}
