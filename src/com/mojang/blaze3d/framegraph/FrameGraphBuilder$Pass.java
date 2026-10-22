package com.mojang.blaze3d.framegraph;

import com.mojang.blaze3d.resource.ResourceDescriptor;
import com.mojang.blaze3d.resource.ResourceHandle;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class FrameGraphBuilder$Pass implements FramePass {
   private final int id;
   private final String name;
   private final List<FrameGraphBuilder$Handle<?>> writesFrom;
   private final BitSet requiredResourceIds;
   private final BitSet requiredPassIds;
   private Runnable task;
   private final List<FrameGraphBuilder$InternalVirtualResource<?>> resourcesToAcquire;
   private final BitSet resourcesToRelease;
   private boolean disableCulling;

   public FrameGraphBuilder$Pass(final FrameGraphBuilder this$0, final int id, final String name) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.writesFrom = new ArrayList<>();
      this.requiredResourceIds = new BitSet();
      this.requiredPassIds = new BitSet();
      this.task = () -> {};
      this.resourcesToAcquire = new ArrayList<>();
      this.resourcesToRelease = new BitSet();
      this.id = id;
      this.name = name;
   }

   private <T> void markResourceRequired(final FrameGraphBuilder$Handle<T> handle) {
      if (handle.holder instanceof FrameGraphBuilder$InternalVirtualResource<?> resource) {
         this.requiredResourceIds.set(resource.id);
      }
   }

   private void markPassRequired(final FrameGraphBuilder$Pass pass) {
      this.requiredPassIds.set(pass.id);
   }

   @Override
   public <T> ResourceHandle<T> createsInternal(final String name, final ResourceDescriptor<T> descriptor) {
      FrameGraphBuilder$InternalVirtualResource<T> resource = this.this$0.createInternalResource(name, descriptor, this);
      this.requiredResourceIds.set(resource.id);
      return resource.handle;
   }

   @Override
   public <T> void reads(final ResourceHandle<T> handle) {
      this._reads((FrameGraphBuilder$Handle<T>)handle);
   }

   private <T> void _reads(final FrameGraphBuilder$Handle<T> handle) {
      this.markResourceRequired(handle);
      if (handle.createdBy != null) {
         this.markPassRequired(handle.createdBy);
      }

      handle.readBy.set(this.id);
   }

   @Override
   public <T> ResourceHandle<T> readsAndWrites(final ResourceHandle<T> handle) {
      return this._readsAndWrites((FrameGraphBuilder$Handle<T>)handle);
   }

   @Override
   public void requires(final FramePass pass) {
      this.requiredPassIds.set(((FrameGraphBuilder$Pass)pass).id);
   }

   @Override
   public void disableCulling() {
      this.disableCulling = true;
   }

   private <T> FrameGraphBuilder$Handle<T> _readsAndWrites(final FrameGraphBuilder$Handle<T> handle) {
      this.writesFrom.add(handle);
      this._reads(handle);
      return handle.writeAndAlias(this);
   }

   @Override
   public void executes(final Runnable task) {
      this.task = task;
   }

   @Override
   public String toString() {
      return this.name;
   }
}
