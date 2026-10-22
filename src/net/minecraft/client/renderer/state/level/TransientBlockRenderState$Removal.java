package net.minecraft.client.renderer.state.level;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record TransientBlockRenderState$Removal(long sectionNode, long compileTaskStartTimeNs) {
}
