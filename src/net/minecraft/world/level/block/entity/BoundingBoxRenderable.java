package net.minecraft.world.level.block.entity;

public interface BoundingBoxRenderable {
   BoundingBoxRenderable$Mode renderMode();

   BoundingBoxRenderable$RenderableBox getRenderableBox();
}
