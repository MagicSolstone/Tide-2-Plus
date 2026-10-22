package net.minecraft.client.resources.palette;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.DynamicAtlasTreeSlot;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.metadata.texture.PaletteMetadataSection;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.PreparableReloadListener$PreparationBarrier;
import net.minecraft.server.packs.resources.PreparableReloadListener$SharedState;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class PalettedTextureManager implements PreparableReloadListener, AutoCloseable {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final TextureManager textureManager;
   private BiFunction<Identifier, Identifier, PaletteMapping> paletteMappings = (var0, var1) -> PaletteMapping.NONE;
   private final LoadingCache<Identifier, Optional<PalettedTextureManager$BaseTexture>> baseTextureCache;
   private final List<PalettedTextureManager$AtlasTexture> atlasTextures = new ArrayList<>();
   private final List<PalettedTextureManager$OverflowTexture> overflowTextures = new ArrayList<>();
   private final Map<PalettedTextureManager$SlotKey, PalettedTextureManager$Handle> handles = new HashMap<>();
   private final PalettedTextureManager$Handle missingHandle;

   public PalettedTextureManager(final ResourceManager resourceManager, final TextureManager textureManager) {
      this.textureManager = textureManager;
      this.baseTextureCache = CacheBuilder.newBuilder()
         .expireAfterAccess(Duration.ofMinutes(5L))
         .maximumSize(64L)
         .removalListener(notification -> ((Optional)notification.getValue()).ifPresent(PalettedTextureManager$BaseTexture::close))
         .build(new PalettedTextureManager$1(this, resourceManager));
      this.missingHandle = new PalettedTextureManager$2(this);
   }

   @Nullable
   private static PalettedTextureManager$BaseTexture loadBaseTexture(final Identifier id, final ResourceManager resourceManager) {
      Identifier location = id.withPath(path -> "textures/" + path + ".png");

      try {
         Resource resource = resourceManager.getResourceOrThrow(location);

         NativeImage image;
         try (InputStream input = resource.open()) {
            image = NativeImage.read(input);
         }

         return new PalettedTextureManager$BaseTexture(image, resource.metadata().getSection(PaletteMetadataSection.TYPE).orElse(null));
      } catch (IOException var10) {
         LOGGER.error("Failed to load paletted base texture at {}", location, var10);
         return null;
      }
   }

   public PalettedTextureManager$Handle getOrPrepare(final Identifier baseTexture, final Identifier paletteId) {
      PalettedTextureManager$SlotKey key = new PalettedTextureManager$SlotKey(baseTexture, paletteId);
      PalettedTextureManager$Handle existingHandle = this.handles.get(key);
      if (existingHandle != null) {
         return existingHandle;
      } else {
         PalettedTextureManager$Handle handle = this.prepareSlot(baseTexture, paletteId);
         this.handles.put(key, handle);
         return handle;
      }
   }

   private PalettedTextureManager$Handle prepareSlot(final Identifier baseTextureId, final Identifier paletteId) {
      Optional<PalettedTextureManager$BaseTexture> maybeBaseTexture = (Optional<PalettedTextureManager$BaseTexture>)this.baseTextureCache
         .getUnchecked(baseTextureId);
      if (maybeBaseTexture.isEmpty()) {
         return this.missingHandle;
      } else {
         PalettedTextureManager$BaseTexture baseTexture = maybeBaseTexture.get();
         PaletteMetadataSection paletteMetadata = baseTexture.paletteMetadata();
         PaletteMapping paletteMapping = paletteMetadata != null ? this.paletteMappings.apply(paletteMetadata.basePalette(), paletteId) : PaletteMapping.NONE;

         PalettedTextureManager$Slot var9;
         try (NativeImage newTexture = baseTexture.image().mappedCopy(paletteMapping)) {
            PalettedTextureManager$Slot slot = this.allocateSlot(newTexture.getWidth(), newTexture.getHeight());
            RenderSystem.getDevice().createCommandEncoder().writeToTexture(slot.texture.getTexture(), newTexture, 0, 0, slot.x, slot.y);
            var9 = slot;
         }

         return var9;
      }
   }

   private PalettedTextureManager$Slot allocateSlot(final int width, final int height) {
      if (width <= 512 && height <= 512) {
         for (PalettedTextureManager$AtlasTexture atlas : this.atlasTextures) {
            DynamicAtlasTreeSlot atlasSlot = atlas.tryAllocateSlot(width, height);
            if (atlasSlot != null) {
               return new PalettedTextureManager$Slot(atlas, atlas.location, atlasSlot.x(), atlasSlot.y(), atlasSlot.width(), atlasSlot.height());
            }
         }

         PalettedTextureManager$AtlasTexture atlasx = new PalettedTextureManager$AtlasTexture(
            Identifier.withDefaultNamespace("paletted/atlas_" + this.atlasTextures.size())
         );
         this.textureManager.register(atlasx.location, atlasx);
         this.atlasTextures.add(atlasx);
         DynamicAtlasTreeSlot atlasSlot = atlasx.tryAllocateSlot(width, height);
         if (atlasSlot == null) {
            throw new IllegalStateException("Could not allocate slot in fresh atlas for sprite with size " + width + "x" + height);
         } else {
            return new PalettedTextureManager$Slot(atlasx, atlasx.location, atlasSlot.x(), atlasSlot.y(), atlasSlot.width(), atlasSlot.height());
         }
      } else {
         PalettedTextureManager$OverflowTexture texture = new PalettedTextureManager$OverflowTexture(
            Identifier.withDefaultNamespace("paletted/overflow_" + this.overflowTextures.size()), width, height
         );
         this.textureManager.register(texture.location, texture);
         this.overflowTextures.add(texture);
         return new PalettedTextureManager$Slot(texture, texture.location, 0, 0, width, height);
      }
   }

   @Override
   public void close() {
      for (PalettedTextureManager$AtlasTexture atlas : this.atlasTextures) {
         this.textureManager.release(atlas.location);
      }

      for (PalettedTextureManager$OverflowTexture texture : this.overflowTextures) {
         this.textureManager.release(texture.location);
      }

      this.atlasTextures.clear();
      this.overflowTextures.clear();
      this.handles.clear();
      this.paletteMappings = (var0, var1) -> PaletteMapping.NONE;
      this.baseTextureCache.invalidateAll();
   }

   @Override
   public CompletableFuture<Void> reload(
      final PreparableReloadListener$SharedState currentReload,
      final Executor taskExecutor,
      final PreparableReloadListener$PreparationBarrier preparationBarrier,
      final Executor reloadExecutor
   ) {
      return Palette.listAndLoad(currentReload.resourceManager(), taskExecutor).thenCompose(preparationBarrier::wait).thenAcceptAsync(palettes -> {
         this.close();
         PaletteMappingCache paletteMappings = new PaletteMappingCache((Map<Identifier, Palette>)palettes);
         this.paletteMappings = paletteMappings::get;
      }, reloadExecutor);
   }
}
