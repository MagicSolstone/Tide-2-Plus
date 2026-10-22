package net.minecraft.client.gui.font;

import com.google.common.cache.CacheLoader;
import java.util.Objects;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GlyphSource;
import net.minecraft.client.renderer.PlayerSkinRenderCache$RenderInfo;
import net.minecraft.network.chat.FontDescription$PlayerSprite;

@Environment(EnvType.CLIENT)
class PlayerGlyphProvider$1 extends CacheLoader<FontDescription$PlayerSprite, GlyphSource> {
   PlayerGlyphProvider$1(final PlayerGlyphProvider this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   public GlyphSource load(final FontDescription$PlayerSprite playerInfo) {
      Supplier<PlayerSkinRenderCache$RenderInfo> skin = this.this$0.playerSkinRenderCache.createLookup(playerInfo.profile());
      boolean hat = playerInfo.hat();
      return new SingleSpriteSource(new PlayerGlyphProvider$1$1(this, skin, hat));
   }
}
