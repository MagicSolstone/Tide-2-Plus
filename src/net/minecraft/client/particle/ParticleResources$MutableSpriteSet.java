package net.minecraft.client.particle;

import com.google.common.collect.ImmutableList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
class ParticleResources$MutableSpriteSet implements SpriteSet {
   private List<TextureAtlasSprite> sprites;

   private ParticleResources$MutableSpriteSet() {
   }

   @Override
   public TextureAtlasSprite get(final int index, final int max) {
      return this.sprites.get(index * (this.sprites.size() - 1) / max);
   }

   @Override
   public TextureAtlasSprite get(final RandomSource random) {
      return this.sprites.get(random.nextInt(this.sprites.size()));
   }

   @Override
   public TextureAtlasSprite first() {
      return this.sprites.getFirst();
   }

   public void rebind(final List<TextureAtlasSprite> ids) {
      this.sprites = ImmutableList.copyOf(ids);
   }
}
