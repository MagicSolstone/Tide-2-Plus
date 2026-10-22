package com.mojang.blaze3d.framegraph;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface FrameGraphBuilder$Inspector {
   FrameGraphBuilder$Inspector NONE = new FrameGraphBuilder$Inspector$1();

   default void acquireResource(final String name) {
   }

   default void releaseResource(final String name) {
   }

   default void beforeExecutePass(final String name) {
   }

   default void afterExecutePass(final String name) {
   }
}
