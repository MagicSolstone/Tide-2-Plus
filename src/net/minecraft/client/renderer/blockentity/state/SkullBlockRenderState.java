package net.minecraft.client.renderer.blockentity.state;

import com.mojang.math.Transformation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.level.block.SkullBlock$Type;
import net.minecraft.world.level.block.SkullBlock$Types;

@Environment(EnvType.CLIENT)
public class SkullBlockRenderState extends BlockEntityRenderState {
   public float animationProgress;
   public Transformation transformation = Transformation.IDENTITY;
   public SkullBlock$Type skullType = SkullBlock$Types.ZOMBIE;
   public RenderType renderType;
}
