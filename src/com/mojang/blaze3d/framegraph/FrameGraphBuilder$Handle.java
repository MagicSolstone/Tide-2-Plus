package com.mojang.blaze3d.framegraph;

import com.mojang.blaze3d.resource.ResourceHandle;
import java.util.BitSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class FrameGraphBuilder$Handle<T> implements ResourceHandle<T> {
   private final FrameGraphBuilder$VirtualResource<T> holder;
   private final int version;
   private final FrameGraphBuilder$Pass createdBy;
   private final BitSet readBy = new BitSet();
   @Nullable
   private FrameGraphBuilder$Handle<T> aliasedBy;

   private FrameGraphBuilder$Handle(final FrameGraphBuilder$VirtualResource<T> holder, final int version, final FrameGraphBuilder$Pass createdBy) {
      this.holder = holder;
      this.version = version;
      this.createdBy = createdBy;
   }

   @Override
   public T get() {
      return this.holder.get();
   }

   private FrameGraphBuilder$Handle<T> writeAndAlias(final FrameGraphBuilder$Pass pass) {
      if (this.holder.handle != this) {
         throw new IllegalStateException("Handle " + this + " is no longer valid, as its contents were moved into " + this.aliasedBy);
      } else {
         FrameGraphBuilder$Handle<T> newHandle = new FrameGraphBuilder$Handle<>(this.holder, this.version + 1, pass);
         this.holder.handle = newHandle;
         this.aliasedBy = newHandle;
         return newHandle;
      }
   }

   @Override
   public String toString() {
      return this.createdBy != null ? this.holder + "#" + this.version + " (from " + this.createdBy + ")" : this.holder + "#" + this.version;
   }
}
