package net.minecraft.client.resources.model.sprite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.SpriteLoader$Preparations;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class AtlasManager$PendingStitchResults {
   private final List<AtlasManager$PendingStitch> pendingStitches;
   private final Map<Identifier, CompletableFuture<SpriteLoader$Preparations>> stitchFuturesById;
   private final CompletableFuture<?> allReadyToUpload;

   private AtlasManager$PendingStitchResults(
      final List<AtlasManager$PendingStitch> pendingStitches,
      final Map<Identifier, CompletableFuture<SpriteLoader$Preparations>> stitchFuturesById,
      final CompletableFuture<?> allReadyToUpload
   ) {
      this.pendingStitches = pendingStitches;
      this.stitchFuturesById = stitchFuturesById;
      this.allReadyToUpload = allReadyToUpload;
   }

   public Map<SpriteId, TextureAtlasSprite> joinAndUpload() {
      Map<SpriteId, TextureAtlasSprite> result = new HashMap<>();
      this.pendingStitches.forEach(pendingStitch -> pendingStitch.joinAndUpload(result));
      return result;
   }

   public CompletableFuture<SpriteLoader$Preparations> get(final Identifier atlasId) {
      return Objects.requireNonNull(this.stitchFuturesById.get(atlasId));
   }
}
