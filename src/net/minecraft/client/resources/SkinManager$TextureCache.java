package net.minecraft.client.resources;

import com.google.common.hash.Hashing;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.ClientAsset$Texture;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
class SkinManager$TextureCache {
   private final Path root;
   private final Type type;
   private final Map<String, CompletableFuture<ClientAsset$Texture>> textures;

   private SkinManager$TextureCache(final SkinManager this$0, final Path root, final Type type) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.textures = new Object2ObjectOpenHashMap();
      this.root = root;
      this.type = type;
   }

   public CompletableFuture<ClientAsset$Texture> getOrLoad(final MinecraftProfileTexture texture) {
      String hash = texture.getHash();
      CompletableFuture<ClientAsset$Texture> future = this.textures.get(hash);
      if (future == null) {
         future = this.registerTexture(texture);
         this.textures.put(hash, future);
      }

      return future;
   }

   private CompletableFuture<ClientAsset$Texture> registerTexture(final MinecraftProfileTexture textureInfo) {
      String hash = Hashing.sha1().hashUnencodedChars(textureInfo.getHash()).toString();
      Identifier textureId = this.getTextureLocation(hash);
      Path file = this.root.resolve(hash.length() > 2 ? hash.substring(0, 2) : "xx").resolve(hash);
      return this.this$0.skinTextureDownloader.downloadAndRegisterSkin(textureId, file, textureInfo.getUrl(), this.type == Type.SKIN);
   }

   private Identifier getTextureLocation(final String textureHash) {
      String root = switch (this.type) {
         case SKIN -> "skins";
         case CAPE -> "capes";
         case ELYTRA -> "elytra";
         default -> throw new MatchException(null, null);
      };
      return Identifier.withDefaultNamespace(root + "/" + textureHash);
   }
}
