package com.mojang.blaze3d.framegraph;

import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.blaze3d.resource.ResourceDescriptor;
import com.mojang.blaze3d.resource.ResourceHandle;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class FrameGraphBuilder {
   private final List<FrameGraphBuilder$InternalVirtualResource<?>> internalResources = new ArrayList<>();
   private final List<FrameGraphBuilder$ExternalResource<?>> externalResources = new ArrayList<>();
   private final List<FrameGraphBuilder$Pass> passes = new ArrayList<>();

   public FramePass addPass(final String name) {
      FrameGraphBuilder$Pass pass = new FrameGraphBuilder$Pass(this, this.passes.size(), name);
      this.passes.add(pass);
      return pass;
   }

   public <T> ResourceHandle<T> importExternal(final String name, final T resource) {
      FrameGraphBuilder$ExternalResource<T> holder = new FrameGraphBuilder$ExternalResource<>(name, null, resource);
      this.externalResources.add(holder);
      return holder.handle;
   }

   public <T> ResourceHandle<T> createInternal(final String name, final ResourceDescriptor<T> descriptor) {
      return this.createInternalResource(name, descriptor, null).handle;
   }

   private <T> FrameGraphBuilder$InternalVirtualResource<T> createInternalResource(
      final String name, final ResourceDescriptor<T> descriptor, final FrameGraphBuilder$Pass createdBy
   ) {
      int id = this.internalResources.size();
      FrameGraphBuilder$InternalVirtualResource<T> resource = new FrameGraphBuilder$InternalVirtualResource<>(id, name, createdBy, descriptor);
      this.internalResources.add(resource);
      return resource;
   }

   public void execute(final GraphicsResourceAllocator resourceAllocator) {
      this.execute(resourceAllocator, FrameGraphBuilder$Inspector.NONE);
   }

   public void execute(final GraphicsResourceAllocator resourceAllocator, final FrameGraphBuilder$Inspector inspector) {
      BitSet passesToKeep = this.identifyPassesToKeep();
      List<FrameGraphBuilder$Pass> passesInOrder = new ArrayList<>(passesToKeep.cardinality());
      BitSet visiting = new BitSet(this.passes.size());

      for (FrameGraphBuilder$Pass pass : this.passes) {
         this.resolvePassOrder(pass, passesToKeep, visiting, passesInOrder);
      }

      this.assignResourceLifetimes(passesInOrder);

      for (FrameGraphBuilder$Pass pass : passesInOrder) {
         for (FrameGraphBuilder$InternalVirtualResource<?> resource : pass.resourcesToAcquire) {
            inspector.acquireResource(resource.name);
            resource.acquire(resourceAllocator);
         }

         inspector.beforeExecutePass(pass.name);
         pass.task.run();
         inspector.afterExecutePass(pass.name);

         for (int id = pass.resourcesToRelease.nextSetBit(0); id >= 0; id = pass.resourcesToRelease.nextSetBit(id + 1)) {
            FrameGraphBuilder$InternalVirtualResource<?> resource = this.internalResources.get(id);
            inspector.releaseResource(resource.name);
            resource.release(resourceAllocator);
         }
      }
   }

   private BitSet identifyPassesToKeep() {
      Deque<FrameGraphBuilder$Pass> scratchQueue = new ArrayDeque<>(this.passes.size());
      BitSet passesToKeep = new BitSet(this.passes.size());

      for (FrameGraphBuilder$VirtualResource<?> resource : this.externalResources) {
         FrameGraphBuilder$Pass pass = resource.handle.createdBy;
         if (pass != null) {
            this.discoverAllRequiredPasses(pass, passesToKeep, scratchQueue);
         }
      }

      for (FrameGraphBuilder$Pass pass : this.passes) {
         if (pass.disableCulling) {
            this.discoverAllRequiredPasses(pass, passesToKeep, scratchQueue);
         }
      }

      return passesToKeep;
   }

   private void discoverAllRequiredPasses(final FrameGraphBuilder$Pass sourcePass, final BitSet visited, final Deque<FrameGraphBuilder$Pass> passesToTrace) {
      passesToTrace.add(sourcePass);

      while (!passesToTrace.isEmpty()) {
         FrameGraphBuilder$Pass pass = passesToTrace.poll();
         if (!visited.get(pass.id)) {
            visited.set(pass.id);

            for (int id = pass.requiredPassIds.nextSetBit(0); id >= 0; id = pass.requiredPassIds.nextSetBit(id + 1)) {
               passesToTrace.add(this.passes.get(id));
            }
         }
      }
   }

   private void resolvePassOrder(final FrameGraphBuilder$Pass pass, final BitSet passesToFind, final BitSet visiting, final List<FrameGraphBuilder$Pass> output) {
      if (visiting.get(pass.id)) {
         String involvedPasses = visiting.stream().mapToObj(idx -> this.passes.get(idx).name).collect(Collectors.joining(", "));
         throw new IllegalStateException("Frame graph cycle detected between " + involvedPasses);
      } else if (passesToFind.get(pass.id)) {
         visiting.set(pass.id);
         passesToFind.clear(pass.id);

         for (int id = pass.requiredPassIds.nextSetBit(0); id >= 0; id = pass.requiredPassIds.nextSetBit(id + 1)) {
            this.resolvePassOrder(this.passes.get(id), passesToFind, visiting, output);
         }

         for (FrameGraphBuilder$Handle<?> handle : pass.writesFrom) {
            for (int id = handle.readBy.nextSetBit(0); id >= 0; id = handle.readBy.nextSetBit(id + 1)) {
               if (id != pass.id) {
                  this.resolvePassOrder(this.passes.get(id), passesToFind, visiting, output);
               }
            }
         }

         output.add(pass);
         visiting.clear(pass.id);
      }
   }

   private void assignResourceLifetimes(final Collection<FrameGraphBuilder$Pass> passesInOrder) {
      FrameGraphBuilder$Pass[] lastPassByResource = new FrameGraphBuilder$Pass[this.internalResources.size()];

      for (FrameGraphBuilder$Pass pass : passesInOrder) {
         for (int id = pass.requiredResourceIds.nextSetBit(0); id >= 0; id = pass.requiredResourceIds.nextSetBit(id + 1)) {
            FrameGraphBuilder$InternalVirtualResource<?> resource = this.internalResources.get(id);
            FrameGraphBuilder$Pass lastPass = lastPassByResource[id];
            lastPassByResource[id] = pass;
            if (lastPass == null) {
               pass.resourcesToAcquire.add(resource);
            } else {
               lastPass.resourcesToRelease.clear(id);
            }

            pass.resourcesToRelease.set(id);
         }
      }
   }
}
