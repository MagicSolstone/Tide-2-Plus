package net.minecraft.client.gui.font;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.font.GlyphInfo;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GlyphSource;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.network.chat.FontDescription$PlayerSprite;

@Environment(EnvType.CLIENT)
public class PlayerGlyphProvider {
   private static final GlyphInfo GLYPH_INFO = GlyphInfo.simple(8.0F);
   private final PlayerSkinRenderCache playerSkinRenderCache;
   private final LoadingCache<FontDescription$PlayerSprite, GlyphSource> wrapperCache = CacheBuilder.newBuilder()
      .expireAfterAccess(PlayerSkinRenderCache.CACHE_DURATION)
      .build(new PlayerGlyphProvider$1(this));

   public PlayerGlyphProvider(final PlayerSkinRenderCache playerSkinRenderCache) {
      this.playerSkinRenderCache = playerSkinRenderCache;
   }

   public GlyphSource sourceForPlayer(final FontDescription$PlayerSprite playerInfo) {
      return (GlyphSource)this.wrapperCache.getUnchecked(playerInfo);
   }
}
