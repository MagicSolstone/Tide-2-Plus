package net.minecraft.client.renderer.block.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Matrix4fc;

@Environment(EnvType.CLIENT)
public interface BlockModel$Unbaked {
   BlockModel bake(BlockModel$BakingContext context, Matrix4fc transformation);
}
