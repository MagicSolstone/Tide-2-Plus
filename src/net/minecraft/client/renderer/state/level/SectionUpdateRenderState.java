package net.minecraft.client.renderer.state.level;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.chunk.RenderSectionRegion;

@Environment(EnvType.CLIENT)
public record SectionUpdateRenderState(long sectionNode, boolean playerChanged, RenderSectionRegion region) {
}
