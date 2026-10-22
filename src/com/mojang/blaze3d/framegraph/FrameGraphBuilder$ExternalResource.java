package com.mojang.blaze3d.framegraph;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class FrameGraphBuilder$ExternalResource<T> extends FrameGraphBuilder$VirtualResource<T> {
   private final T resource;

   public FrameGraphBuilder$ExternalResource(final String name, final FrameGraphBuilder$Pass createdBy, final T resource) {
      super(name, createdBy);
      this.resource = resource;
   }

   @Override
   public T get() {
      return this.resource;
   }
}
