package net.minecraft.client.resources.palette;

import com.google.common.cache.CacheLoader;
import java.util.Objects;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;

@Environment(EnvType.CLIENT)
class PalettedTextureManager$1 extends CacheLoader<Identifier, Optional<PalettedTextureManager$BaseTexture>> {
   PalettedTextureManager$1(final PalettedTextureManager this$0, final ResourceManager val$resourceManager) {
      this.val$resourceManager = val$resourceManagerx;
      Objects.requireNonNull(this$0);
      super();
   }

   public Optional<PalettedTextureManager$BaseTexture> load(final Identifier id) {
      return Optional.ofNullable(PalettedTextureManager.loadBaseTexture(id, this.val$resourceManager));
   }
}
