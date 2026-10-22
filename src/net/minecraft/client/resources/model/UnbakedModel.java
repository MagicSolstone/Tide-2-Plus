package net.minecraft.client.resources.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.cuboid.ItemTransforms;
import net.minecraft.client.resources.model.geometry.UnbakedGeometry;
import net.minecraft.client.resources.model.sprite.TextureSlots$Data;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public interface UnbakedModel {
   String PARTICLE_TEXTURE_REFERENCE = "particle";

   @Nullable
   default Boolean ambientOcclusion() {
      return null;
   }

   @Nullable
   default UnbakedModel$GuiLight guiLight() {
      return null;
   }

   @Nullable
   default ItemTransforms transforms() {
      return null;
   }

   default TextureSlots$Data textureSlots() {
      return TextureSlots$Data.EMPTY;
   }

   @Nullable
   default UnbakedGeometry geometry() {
      return null;
   }

   @Nullable
   default Identifier parent() {
      return null;
   }
}
