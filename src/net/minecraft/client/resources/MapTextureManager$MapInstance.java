package net.minecraft.client.resources;

import com.mojang.blaze3d.platform.NativeImage;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

@Environment(EnvType.CLIENT)
class MapTextureManager$MapInstance implements AutoCloseable {
   private MapItemSavedData data;
   private final DynamicTexture texture;
   private boolean requiresUpload;
   private final Identifier location;

   private MapTextureManager$MapInstance(final MapTextureManager this$0, final int id, final MapItemSavedData data) {
      Objects.requireNonNull(this$0);
      super();
      this.requiresUpload = true;
      this.data = data;
      this.texture = new DynamicTexture(() -> "Map " + id, 128, 128, true);
      this.location = Identifier.withDefaultNamespace("map/" + id);
      this$0.textureManager.register(this.location, this.texture);
   }

   private void replaceMapData(final MapItemSavedData data) {
      boolean dataChanged = this.data != data;
      this.data = data;
      this.requiresUpload |= dataChanged;
   }

   public void forceUpload() {
      this.requiresUpload = true;
   }

   private void updateTextureIfNeeded() {
      if (this.requiresUpload) {
         NativeImage pixels = this.texture.getPixels();

         for (int y = 0; y < 128; y++) {
            for (int x = 0; x < 128; x++) {
               int i = x + y * 128;
               pixels.setPixel(x, y, MapColor.getColorFromPackedId(this.data.colors[i]));
            }
         }

         this.texture.upload();
         this.requiresUpload = false;
      }
   }

   @Override
   public void close() {
      this.texture.close();
   }
}
