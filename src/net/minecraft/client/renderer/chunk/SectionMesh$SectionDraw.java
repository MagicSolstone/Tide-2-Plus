package net.minecraft.client.renderer.chunk;

import com.mojang.renderpearl.api.pipeline.IndexType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record SectionMesh$SectionDraw(int indexCount, IndexType indexType, boolean hasCustomIndexBuffer) {
}
