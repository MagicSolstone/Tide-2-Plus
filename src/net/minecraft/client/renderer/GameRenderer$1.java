package net.minecraft.client.renderer;

import com.mojang.renderpearl.api.pipeline.ShaderSource;
import com.mojang.renderpearl.api.pipeline.ShaderSource$CachedIncludeSource;
import com.mojang.renderpearl.api.pipeline.ShaderType;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class GameRenderer$1 implements ShaderSource {
   GameRenderer$1(final ResourceManager val$resourceManager, final Map val$includes) {
      this.val$resourceManager = val$resourceManagerx;
      this.val$includes = val$includesx;
   }

   @Nullable
   @Override
   public String getShader(final Identifier id, final ShaderType type) {
      Identifier location = type.idConverter().idToFile(id);

      try {
         return this.val$resourceManager.getResourceOrThrow(location).readAllAsString();
      } catch (Exception var5) {
         GameRenderer.LOGGER.error("Couldn't preload shader {}", location, var5);
         return null;
      }
   }

   @Nullable
   @Override
   public ShaderSource$CachedIncludeSource getInclude(final Identifier id) {
      return (ShaderSource$CachedIncludeSource)this.val$includes.get(id);
   }

   @Override
   public void close() {
      this.val$includes.values().forEach(ShaderSource$CachedIncludeSource::close);
   }
}
