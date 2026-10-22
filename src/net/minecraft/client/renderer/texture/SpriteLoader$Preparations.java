package net.minecraft.client.renderer.texture;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record SpriteLoader$Preparations(
   int width, int height, int mipLevel, TextureAtlasSprite missing, Map<Identifier, TextureAtlasSprite> regions, CompletableFuture<Void> readyForUpload
) {
   @Nullable
   public TextureAtlasSprite getSprite(final Identifier id) {
      return this.regions.get(id);
   }
}
