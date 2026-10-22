package net.minecraft.client.renderer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class ShaderManager$PostChainCache implements AutoCloseable {
   private final ShaderManager$Configs configs;
   private final Map<Identifier, Optional<PostChain>> postChains;
   private boolean triggeredRecovery;

   private ShaderManager$PostChainCache(final ShaderManager this$0, final ShaderManager$Configs configs) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.postChains = new HashMap<>();
      this.configs = configs;
   }

   @Nullable
   public PostChain getOrLoadPostChain(final Identifier id, final Set<Identifier> allowedTargets) throws ShaderManager$CompilationException {
      Optional<PostChain> cached = this.postChains.get(id);
      if (cached != null) {
         return cached.orElse(null);
      } else {
         PostChain postChain = this.loadPostChain(id, allowedTargets);
         this.postChains.put(id, Optional.ofNullable(postChain));
         return postChain;
      }
   }

   @Nullable
   private PostChain loadPostChain(final Identifier id, final Set<Identifier> allowedTargets) throws ShaderManager$CompilationException {
      PostChainConfig config = this.configs.postChains.get(id);
      if (config == null) {
         if (!id.equals(GameRenderer.END_OF_FRAME_POST_EFFECT)) {
            ShaderManager.LOGGER.warn("Attempted to load a non-existent post effect {}", id);
         }

         return null;
      } else {
         return PostChain.load(
            config, this.this$0.textureManager, allowedTargets, id, this.this$0.postChainProjection, this.this$0.postChainProjectionMatrixBuffer
         );
      }
   }

   @Override
   public void close() {
      this.postChains.values().forEach(chain -> chain.ifPresent(PostChain::close));
      this.postChains.clear();
   }

   public Stream<Identifier> getKnownPostEffects() {
      return this.configs.postChains.keySet().stream();
   }
}
