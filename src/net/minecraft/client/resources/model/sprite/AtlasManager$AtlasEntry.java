package net.minecraft.client.resources.model.sprite;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.SpriteLoader;
import net.minecraft.client.renderer.texture.SpriteLoader$Preparations;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.server.packs.resources.ResourceManager;

@Environment(EnvType.CLIENT)
record AtlasManager$AtlasEntry(TextureAtlas atlas, AtlasManager$AtlasConfig config) implements AutoCloseable {
   @Override
   public void close() {
      this.atlas.close();
   }

   private CompletableFuture<SpriteLoader$Preparations> scheduleLoad(final ResourceManager resourceManager, final Executor executor, final int maxMipmapLevels) {
      return SpriteLoader.create(this.atlas)
         .loadAndStitch(
            resourceManager, this.config.definitionLocation, this.config.createMipmaps ? maxMipmapLevels : 0, executor, this.config.additionalMetadata
         );
   }
}
