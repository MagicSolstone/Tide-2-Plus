package net.minecraft.client.resources;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

@Environment(EnvType.CLIENT)
public class MapTextureManager implements AutoCloseable {
   private final Int2ObjectMap<MapTextureManager$MapInstance> maps = new Int2ObjectOpenHashMap();
   private final TextureManager textureManager;

   public MapTextureManager(final TextureManager textureManager) {
      this.textureManager = textureManager;
   }

   public void update(final MapId id, final MapItemSavedData data) {
      this.getOrCreateMapInstance(id, data).forceUpload();
   }

   public Identifier prepareMapTexture(final MapId id, final MapItemSavedData data) {
      MapTextureManager$MapInstance mapInstance = this.getOrCreateMapInstance(id, data);
      mapInstance.updateTextureIfNeeded();
      return mapInstance.location;
   }

   public void resetData() {
      ObjectIterator var1 = this.maps.values().iterator();

      while (var1.hasNext()) {
         MapTextureManager$MapInstance mapInstance = (MapTextureManager$MapInstance)var1.next();
         mapInstance.close();
      }

      this.maps.clear();
   }

   private MapTextureManager$MapInstance getOrCreateMapInstance(final MapId id, final MapItemSavedData data) {
      return (MapTextureManager$MapInstance)this.maps.compute(id.id(), (k, instance) -> {
         if (instance == null) {
            return new MapTextureManager$MapInstance(this, k, data);
         } else {
            instance.replaceMapData(data);
            return instance;
         }
      });
   }

   @Override
   public void close() {
      this.resetData();
   }
}
