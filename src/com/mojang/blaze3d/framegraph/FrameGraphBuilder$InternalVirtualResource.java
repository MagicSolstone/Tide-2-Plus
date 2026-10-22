package com.mojang.blaze3d.framegraph;

import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.blaze3d.resource.ResourceDescriptor;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class FrameGraphBuilder$InternalVirtualResource<T> extends FrameGraphBuilder$VirtualResource<T> {
   private final int id;
   private final ResourceDescriptor<T> descriptor;
   @Nullable
   private T physicalResource;

   public FrameGraphBuilder$InternalVirtualResource(
      final int id, final String name, final FrameGraphBuilder$Pass createdBy, final ResourceDescriptor<T> descriptor
   ) {
      super(name, createdBy);
      this.id = id;
      this.descriptor = descriptor;
   }

   @Override
   public T get() {
      return Objects.requireNonNull(this.physicalResource, "Resource is not currently available");
   }

   public void acquire(final GraphicsResourceAllocator allocator) {
      if (this.physicalResource != null) {
         throw new IllegalStateException("Tried to acquire physical resource, but it was already assigned");
      } else {
         this.physicalResource = allocator.acquire(this.descriptor);
      }
   }

   public void release(final GraphicsResourceAllocator allocator) {
      if (this.physicalResource == null) {
         throw new IllegalStateException("Tried to release physical resource that was not allocated");
      } else {
         allocator.release(this.descriptor, this.physicalResource);
         this.physicalResource = null;
      }
   }
}
