package net.minecraft.client.renderer.blockentity.state;

import com.mojang.math.Transformation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BannerBlock$AttachmentType;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

@Environment(EnvType.CLIENT)
public class BannerRenderState extends BlockEntityRenderState {
   public DyeColor baseColor;
   public BannerPatternLayers patterns = BannerPatternLayers.EMPTY;
   public float phase;
   public Transformation transformation = Transformation.IDENTITY;
   public BannerBlock$AttachmentType attachmentType = BannerBlock$AttachmentType.GROUND;
}
