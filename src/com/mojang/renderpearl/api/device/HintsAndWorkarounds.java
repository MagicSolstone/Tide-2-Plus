package com.mojang.renderpearl.api.device;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record HintsAndWorkarounds(
   boolean writeToBufferIsSlow, boolean anisotropyHasKnownIssues, boolean isExplicitDepthRequired, boolean multiDrawIndirectHasKnownIssues
) {
}
