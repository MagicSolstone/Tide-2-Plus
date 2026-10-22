package net.minecraft.client.resources.model.geometry;

import com.mojang.blaze3d.platform.Transparency;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.Material$Baked;
import net.minecraft.core.Direction;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record BakedQuad$MaterialInfo(
   TextureAtlasSprite sprite,
   ChunkSectionLayer layer,
   RenderType itemRenderType,
   RenderType itemGlintRenderType,
   RenderType itemGlintSpecialRenderType,
   int tintIndex,
   @Nullable Direction shadeDirectionOverride,
   int lightEmission
) {
   public static BakedQuad$MaterialInfo of(
      final Material$Baked material,
      final Transparency transparency,
      final int tintIndex,
      @Nullable final Direction shadeDirectionOverride,
      final int lightEmission
   ) {
      ChunkSectionLayer layer = ChunkSectionLayer.byTransparency(transparency);
      RenderType itemRenderType;
      RenderType itemGlintRenderType;
      RenderType itemGlintSpecialRenderType;
      if (material.sprite().atlasLocation().equals(TextureAtlas.LOCATION_BLOCKS)) {
         itemRenderType = transparency.hasTranslucent() ? Sheets.translucentBlockItemSheet() : Sheets.cutoutBlockItemSheet();
         itemGlintRenderType = transparency.hasTranslucent() ? Sheets.translucentBlockItemGlintSheet() : Sheets.cutoutBlockItemGlintSheet();
         itemGlintSpecialRenderType = transparency.hasTranslucent()
            ? Sheets.translucentBlockItemGlintSpecialSheet()
            : Sheets.cutoutBlockItemGlintSpecialSheet();
      } else {
         itemRenderType = transparency.hasTranslucent() ? Sheets.translucentItemSheet() : Sheets.cutoutItemSheet();
         itemGlintRenderType = transparency.hasTranslucent() ? Sheets.translucentItemGlintSheet() : Sheets.cutoutItemGlintSheet();
         itemGlintSpecialRenderType = transparency.hasTranslucent() ? Sheets.translucentItemGlintSpecialSheet() : Sheets.cutoutItemGlintSpecialSheet();
      }

      return new BakedQuad$MaterialInfo(
         material.sprite(), layer, itemRenderType, itemGlintRenderType, itemGlintSpecialRenderType, tintIndex, shadeDirectionOverride, lightEmission
      );
   }

   public boolean isTinted() {
      return this.tintIndex != -1;
   }

   @BakedQuad$MaterialFlags
   public int flags() {
      int flags = 0;
      flags |= this.layer.translucent() ? 1 : 0;
      return flags | (this.sprite.contents().isAnimated() ? 2 : 0);
   }
}
