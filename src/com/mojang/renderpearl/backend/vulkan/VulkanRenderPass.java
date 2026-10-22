package com.mojang.renderpearl.backend.vulkan;

import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.commands.GpuQueryPool;
import com.mojang.renderpearl.api.commands.RenderPass$RenderArea;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout$UniformDescription;
import com.mojang.renderpearl.api.pipeline.IndexType;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.renderpearl.backend.api.BackendRenderPipeline;
import com.mojang.renderpearl.backend.api.RenderPassBackend;
import com.mojang.renderpearl.backend.vulkan.checkpoints.CheckpointExtension$CheckpointStorage;
import com.mojang.renderpearl.util.TextureViewAndSampler;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import it.unimi.dsi.fastutil.objects.ReferenceList;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.List;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.vulkan.EXTMultiDraw;
import org.lwjgl.vulkan.KHRPushDescriptor;
import org.lwjgl.vulkan.KHRSynchronization2;
import org.lwjgl.vulkan.VK12;
import org.lwjgl.vulkan.VkBufferViewCreateInfo;
import org.lwjgl.vulkan.VkCommandBuffer;
import org.lwjgl.vulkan.VkDescriptorBufferInfo;
import org.lwjgl.vulkan.VkDescriptorImageInfo;
import org.lwjgl.vulkan.VkDrawIndexedIndirectCommand;
import org.lwjgl.vulkan.VkDrawIndirectCommand;
import org.lwjgl.vulkan.VkMultiDrawIndexedInfoEXT;
import org.lwjgl.vulkan.VkMultiDrawInfoEXT;
import org.lwjgl.vulkan.VkRect2D;
import org.lwjgl.vulkan.VkViewport;
import org.lwjgl.vulkan.VkWriteDescriptorSet;
import org.lwjgl.vulkan.VkViewport.Buffer;

@Environment(EnvType.CLIENT)
public class VulkanRenderPass implements RenderPassBackend {
   private final VulkanDevice device;
   private final VulkanCommandEncoder encoder;
   private final CheckpointExtension$CheckpointStorage checkpointStorage;
   @Nullable
   private final RenderPass$RenderArea renderArea;
   private final int outputWidth;
   private final int outputHeight;
   private final boolean hasDepth;
   private final Supplier<String> label;
   protected int pushedDebugGroups = 0;
   private final VkCommandBuffer commandBuffer;
   @Nullable
   protected VulkanRenderPipeline pipeline;
   private boolean anyDescriptorDirty = false;
   protected final ReferenceList<Object> uniforms = new ReferenceArrayList();

   public VulkanRenderPass(
      final VulkanDevice device,
      final VulkanCommandEncoder encoder,
      final VkCommandBuffer commandBuffer,
      final CheckpointExtension$CheckpointStorage checkpointStorage,
      final RenderPass$RenderArea renderArea,
      final int outputWidth,
      final int outputHeight,
      final boolean hasDepth,
      final Supplier<String> label
   ) {
      this.device = device;
      this.encoder = encoder;
      this.commandBuffer = commandBuffer;
      this.checkpointStorage = checkpointStorage;
      this.renderArea = renderArea;
      this.outputWidth = outputWidth;
      this.outputHeight = outputHeight;
      this.hasDepth = hasDepth;
      this.label = label;
      MemoryStack stack = MemoryStack.stackPush();

      try {
         Buffer viewport = VkViewport.calloc(1, stack);
         viewport.x(0.0F);
         viewport.y(0.0F);
         viewport.width(outputWidth);
         viewport.height(outputHeight);
         viewport.minDepth(0.0F);
         viewport.maxDepth(1.0F);
         VK12.vkCmdSetViewport(this.commandBuffer(), 0, viewport);
         setScissor(stack, this.commandBuffer(), renderArea.x(), renderArea.y(), renderArea.width(), renderArea.height());
      } catch (Throwable var14) {
         if (stack != null) {
            try {
               stack.close();
            } catch (Throwable var13) {
               var14.addSuppressed(var13);
            }
         }

         throw var14;
      }

      if (stack != null) {
         stack.close();
      }
   }

   private VkCommandBuffer commandBuffer() {
      return this.commandBuffer;
   }

   @Override
   public void pushDebugGroup(final Supplier<String> label) {
      this.pushedDebugGroups++;
      this.device.instance().debug().beginDebugGroup(this.commandBuffer(), label);
   }

   @Override
   public void popDebugGroup() {
      if (this.pushedDebugGroups == 0) {
         throw new IllegalStateException("Can't pop more debug groups than was pushed!");
      } else {
         this.pushedDebugGroups--;
         this.device.instance().debug().endDebugGroup(this.commandBuffer());
      }
   }

   @Override
   public void setPipeline(final BackendRenderPipeline pipeline) {
      if (pipeline instanceof VulkanRenderPipeline vulkanRenderPipeline) {
         this.pipeline = vulkanRenderPipeline;
         this.anyDescriptorDirty = true;
         this.uniforms.clear();
         this.uniforms.size(vulkanRenderPipeline.uniforms().size());
         VK12.vkCmdBindPipeline(this.commandBuffer(), 0, this.hasDepth ? this.pipeline.withDepthPipeline() : this.pipeline.withoutDepthPipeline());
      } else {
         throw new IllegalArgumentException("Pipeline must be instance of VulkanRenderPipeline");
      }
   }

   @Override
   public void setUniform(final int index, @Nullable final Object value) {
      this.uniforms.set(index, value);
      this.anyDescriptorDirty = true;
   }

   @Override
   public void pushConstants(final ByteBuffer value) {
      assert this.pipeline != null;

      VK12.vkCmdPushConstants(this.commandBuffer(), this.pipeline.pipelineLayout(), Integer.MAX_VALUE, 0, value);
   }

   @Override
   public void enableScissor(final int x, final int y, final int width, final int height) {
      MemoryStack stack = MemoryStack.stackPush();

      try {
         setScissor(stack, this.commandBuffer(), x, y, width, height);
      } catch (Throwable var9) {
         if (stack != null) {
            try {
               stack.close();
            } catch (Throwable var8) {
               var9.addSuppressed(var8);
            }
         }

         throw var9;
      }

      if (stack != null) {
         stack.close();
      }
   }

   private static void setScissor(final MemoryStack stack, final VkCommandBuffer commandBuffer, final int x, final int y, final int width, final int height) {
      org.lwjgl.vulkan.VkRect2D.Buffer scissor = VkRect2D.calloc(1, stack);
      scissor.offset().set(x, y);
      scissor.extent().set(width, height);
      VK12.vkCmdSetScissor(commandBuffer, 0, scissor);
   }

   @Override
   public void disableScissor() {
      if (this.renderArea != null) {
         this.enableScissor(this.renderArea.x(), this.renderArea.y(), this.renderArea.width(), this.renderArea.height());
      } else {
         this.enableScissor(0, 0, this.outputWidth, this.outputHeight);
      }
   }

   @Override
   public void setVertexBuffer(final int slot, @Nullable final GpuBufferSlice vertexBuffer) {
      if (vertexBuffer != null) {
         MemoryStack stack = MemoryStack.stackPush();

         try {
            long buffer = ((VulkanGpuBuffer)vertexBuffer.buffer()).vkBuffer();
            long offset = vertexBuffer.offset();
            VK12.vkCmdBindVertexBuffers(this.commandBuffer(), slot, stack.longs(buffer), stack.longs(offset));
         } catch (Throwable var9) {
            if (stack != null) {
               try {
                  stack.close();
               } catch (Throwable var8) {
                  var9.addSuppressed(var8);
               }
            }

            throw var9;
         }

         if (stack != null) {
            stack.close();
         }
      }
   }

   @Override
   public void setIndexBuffer(final GpuBuffer indexBuffer, final IndexType indexType) {
      int type = switch (indexType) {
         case SHORT -> 0;
         case INT -> 1;
      };
      VK12.vkCmdBindIndexBuffer(this.commandBuffer(), ((VulkanGpuBuffer)indexBuffer).vkBuffer(), 0L, type);
   }

   @Override
   public void drawIndexed(final int indexCount, final int instanceCount, final int firstIndex, final int vertexOffset, final int firstInstance) {
      this.pushDescriptors();
      VK12.vkCmdDrawIndexed(this.commandBuffer(), indexCount, instanceCount, firstIndex, vertexOffset, firstInstance);
   }

   @Override
   public void multiDrawIndexed(final IntBuffer drawParameters, final int instanceCount, final int firstInstance, final int drawCount) {
      this.pushDescriptors();
      EXTMultiDraw.nvkCmdDrawMultiIndexedEXT(
         this.commandBuffer(), drawCount, MemoryUtil.memAddress(drawParameters), instanceCount, firstInstance, VkMultiDrawIndexedInfoEXT.SIZEOF, 0L
      );
   }

   @Override
   public void multiDrawIndexed(final PointerBuffer firstIndexOffsets, final IntBuffer indexCounts, final IntBuffer vertexOffsets, final int drawCount) {
      throw new UnsupportedOperationException("Vulkan does not support the multiDrawDirectSeparate device feature");
   }

   @Override
   public void drawIndexedIndirect(final GpuBufferSlice commands, final int drawCount) {
      this.pushDescriptors();
      VK12.vkCmdDrawIndexedIndirect(
         this.commandBuffer(), ((VulkanGpuBuffer)commands.buffer()).vkBuffer(), commands.offset(), drawCount, VkDrawIndexedIndirectCommand.SIZEOF
      );
   }

   @Override
   public void draw(final int vertexCount, final int instanceCount, final int firstVertex, final int firstInstance) {
      this.pushDescriptors();
      VK12.vkCmdDraw(this.commandBuffer(), vertexCount, instanceCount, firstVertex, firstInstance);
   }

   @Override
   public void multiDraw(final IntBuffer drawParameters, final int instanceCount, final int firstInstance, final int drawCount) {
      this.pushDescriptors();
      EXTMultiDraw.nvkCmdDrawMultiEXT(
         this.commandBuffer(), drawCount, MemoryUtil.memAddress(drawParameters), instanceCount, firstInstance, VkMultiDrawInfoEXT.SIZEOF
      );
   }

   @Override
   public void multiDraw(final IntBuffer firstVertices, final IntBuffer vertexCounts, final int drawCount) {
      throw new UnsupportedOperationException("Vulkan does not support the multiDrawDirectSeparate device feature");
   }

   @Override
   public void drawIndirect(final GpuBufferSlice commands, final int drawCount) {
      this.pushDescriptors();
      VK12.vkCmdDrawIndirect(this.commandBuffer(), ((VulkanGpuBuffer)commands.buffer()).vkBuffer(), commands.offset(), drawCount, VkDrawIndirectCommand.SIZEOF);
   }

   private void pushDescriptors() {
      if (this.anyDescriptorDirty) {
         assert this.pipeline != null;

         List<BindGroupLayout$UniformDescription> uniforms = this.pipeline.uniforms();
         MemoryStack stack = MemoryStack.stackPush();

         try {
            org.lwjgl.vulkan.VkWriteDescriptorSet.Buffer writes = VkWriteDescriptorSet.calloc(uniforms.size(), stack);

            for (int i = 0; i < uniforms.size(); i++) {
               BindGroupLayout$UniformDescription uniform = uniforms.get(i);
               VkWriteDescriptorSet set = ((VkWriteDescriptorSet)writes.get()).sType$Default();
               set.dstBinding(i);
               set.dstArrayElement(0);
               set.descriptorCount(1);
               if (uniform.type() == UniformType.UNIFORM_BUFFER) {
                  GpuBufferSlice buffer = (GpuBufferSlice)this.uniforms.get(i);
                  if (buffer == null) {
                     throw new IllegalStateException("Missing uniform " + uniform.name() + " (should be " + uniform.type() + ")");
                  }

                  org.lwjgl.vulkan.VkDescriptorBufferInfo.Buffer bufferInfo = VkDescriptorBufferInfo.calloc(1, stack);
                  bufferInfo.buffer(((VulkanGpuBuffer)buffer.buffer()).vkBuffer());
                  bufferInfo.offset(buffer.offset());
                  bufferInfo.range(buffer.length());
                  set.descriptorType(6);
                  set.pBufferInfo(bufferInfo);
               } else if (uniform.type() == UniformType.COMBINED_IMAGE_SAMPLER) {
                  TextureViewAndSampler value = (TextureViewAndSampler)this.uniforms.get(i);
                  if (value == null) {
                     throw new IllegalStateException("Missing sampler " + uniform.name());
                  }

                  org.lwjgl.vulkan.VkDescriptorImageInfo.Buffer imageInfo = VkDescriptorImageInfo.calloc(1, stack);
                  imageInfo.sampler(((VulkanGpuSampler)value.sampler()).vkSampler());
                  imageInfo.imageView(((VulkanGpuTextureView)value.view()).vkImageView());
                  imageInfo.imageLayout(1);
                  set.descriptorType(1);
                  set.pImageInfo(imageInfo);
               } else if (uniform.type() == UniformType.TEXEL_BUFFER) {
                  GpuBufferSlice value = (GpuBufferSlice)this.uniforms.get(i);
                  if (value == null) {
                     throw new IllegalStateException("Missing uniform " + uniform.name() + " (should be " + uniform.type() + ")");
                  }

                  LongBuffer bufferViewPtr = stack.callocLong(1);
                  MemoryStack var9 = stack.push();

                  try {
                     assert uniform.gpuFormat() != null;

                     VkBufferViewCreateInfo viewCreateInfo = VkBufferViewCreateInfo.calloc(stack).sType$Default();
                     viewCreateInfo.buffer(((VulkanGpuBuffer)value.buffer()).vkBuffer());
                     viewCreateInfo.offset(value.offset());
                     viewCreateInfo.range(value.length());
                     viewCreateInfo.format(VulkanConst.toVk(uniform.gpuFormat()));
                     VulkanUtils.crashIfFailure(
                        this.device,
                        VK12.vkCreateBufferView(this.device.vkDevice(), viewCreateInfo, null, bufferViewPtr),
                        "Couldn't create buffer view for texel buffer"
                     );
                     long bufferViewHandle = bufferViewPtr.get(0);
                     this.encoder.queueForDestroy(() -> VK12.vkDestroyBufferView(this.device.vkDevice(), bufferViewHandle, null));
                  } catch (Throwable var15) {
                     if (var9 != null) {
                        try {
                           var9.close();
                        } catch (Throwable var14) {
                           var15.addSuppressed(var14);
                        }
                     }

                     throw var15;
                  }

                  if (var9 != null) {
                     var9.close();
                  }

                  set.descriptorType(4);
                  set.pTexelBufferView(bufferViewPtr);
               }
            }

            KHRPushDescriptor.vkCmdPushDescriptorSetKHR(
               this.commandBuffer(), 0, this.pipeline.pipelineLayout(), 0, (org.lwjgl.vulkan.VkWriteDescriptorSet.Buffer)writes.flip()
            );
         } catch (Throwable var16) {
            if (stack != null) {
               try {
                  stack.close();
               } catch (Throwable var13) {
                  var16.addSuppressed(var13);
               }
            }

            throw var16;
         }

         if (stack != null) {
            stack.close();
         }

         this.anyDescriptorDirty = false;
      }
   }

   @Override
   public void writeTimestamp(final GpuQueryPool pool, final int index) {
      long queryPool = ((VulkanQueryPool)pool).vkQueryPool();
      VK12.vkResetQueryPool(this.device.vkDevice(), queryPool, index, 1);
      KHRSynchronization2.vkCmdWriteTimestamp2KHR(this.commandBuffer(), 65536L, queryPool, index);
   }

   public Supplier<String> getLabel() {
      return this.label;
   }
}
