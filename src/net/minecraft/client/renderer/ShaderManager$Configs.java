package net.minecraft.client.renderer;

import com.mojang.renderpearl.api.pipeline.ShaderSource;
import com.mojang.renderpearl.api.pipeline.ShaderSource$CachedIncludeSource;
import com.mojang.renderpearl.api.pipeline.ShaderType;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public record ShaderManager$Configs(
   Map<ShaderManager$ShaderSourceKey, String> shaderSources,
   Map<Identifier, ShaderSource$CachedIncludeSource> includeSources,
   Map<Identifier, PostChainConfig> postChains
) implements ShaderSource {
   public static final ShaderManager$Configs EMPTY = new ShaderManager$Configs(Map.of(), Map.of(), Map.of());

   @Override
   public String getShader(final Identifier id, final ShaderType type) {
      return this.shaderSources.get(new ShaderManager$ShaderSourceKey(id, type));
   }

   @Override
   public ShaderSource$CachedIncludeSource getInclude(final Identifier id) {
      return this.includeSources.get(id);
   }

   @Override
   public void close() {
      this.includeSources.values().forEach(ShaderSource$CachedIncludeSource::close);
   }
}
