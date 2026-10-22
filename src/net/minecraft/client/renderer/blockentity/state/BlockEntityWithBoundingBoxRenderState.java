package net.minecraft.client.renderer.blockentity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.entity.BoundingBoxRenderable$Mode;
import net.minecraft.world.level.block.entity.BoundingBoxRenderable$RenderableBox;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class BlockEntityWithBoundingBoxRenderState extends BlockEntityRenderState {
   public boolean isVisible;
   public BoundingBoxRenderable$Mode mode;
   public BoundingBoxRenderable$RenderableBox box;
   @Nullable
   public BlockEntityWithBoundingBoxRenderState$InvisibleBlockType[] invisibleBlocks;
   @Nullable
   public boolean[] structureVoids;
}
