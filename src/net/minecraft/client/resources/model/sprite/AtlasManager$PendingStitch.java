package net.minecraft.client.resources.model.sprite;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.SpriteLoader$Preparations;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

@Environment(EnvType.CLIENT)
record AtlasManager$PendingStitch(AtlasManager$AtlasEntry entry, CompletableFuture<SpriteLoader$Preparations> preparations) {
   public void joinAndUpload(final Map<SpriteId, TextureAtlasSprite> result) {
      SpriteLoader$Preparations preparations = this.preparations.join();
      this.entry.atlas.upload(preparations);
      preparations.regions().forEach((spriteId, spriteContents) -> result.put(new SpriteId(this.entry.config.textureId, spriteId), spriteContents));
   }
}
