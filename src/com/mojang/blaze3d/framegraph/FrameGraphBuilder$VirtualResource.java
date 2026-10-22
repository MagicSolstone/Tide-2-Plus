package com.mojang.blaze3d.framegraph;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
abstract class FrameGraphBuilder$VirtualResource<T> {
   public final String name;
   public FrameGraphBuilder$Handle<T> handle;

   public FrameGraphBuilder$VirtualResource(final String name, final FrameGraphBuilder$Pass createdBy) {
      this.name = name;
      this.handle = new FrameGraphBuilder$Handle<>(this, 0, createdBy);
   }

   public abstract T get();

   @Override
   public String toString() {
      return this.name;
   }
}
