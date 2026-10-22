package net.minecraft.client.renderer.special;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BannerBlock$AttachmentType;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

@Environment(EnvType.CLIENT)
public record BannerSpecialRenderer$Unbaked(DyeColor baseColor, BannerBlock$AttachmentType attachment)
   implements SpecialModelRenderer$Unbaked<BannerPatternLayers> {
   public static final MapCodec<BannerSpecialRenderer$Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            DyeColor.CODEC.fieldOf("color").forGetter(BannerSpecialRenderer$Unbaked::baseColor),
            BannerBlock$AttachmentType.CODEC
               .optionalFieldOf("attachment", BannerBlock$AttachmentType.GROUND)
               .forGetter(BannerSpecialRenderer$Unbaked::attachment)
         )
         .apply(i, BannerSpecialRenderer$Unbaked::new)
   );

   @Override
   public MapCodec<BannerSpecialRenderer$Unbaked> type() {
      return MAP_CODEC;
   }

   public BannerSpecialRenderer bake(final SpecialModelRenderer$BakingContext context) {
      return new BannerSpecialRenderer(this.baseColor, new BannerRenderer(context), this.attachment);
   }
}
