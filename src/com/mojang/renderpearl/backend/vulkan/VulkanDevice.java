package com.mojang.renderpearl.backend.vulkan;

import com.mojang.logging.LogUtils;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBuffer$Usage;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.commands.GpuQueryPool;
import com.mojang.renderpearl.api.device.DeviceFeatures;
import com.mojang.renderpearl.api.device.DeviceInfo;
import com.mojang.renderpearl.api.device.DeviceLimits;
import com.mojang.renderpearl.api.device.HintsAndWorkarounds;
import com.mojang.renderpearl.api.textures.AddressMode;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.textures.GpuSampler;
import com.mojang.renderpearl.api.textures.GpuTexture;
import com.mojang.renderpearl.api.textures.GpuTexture$Usage;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import com.mojang.renderpearl.backend.api.BackendRenderPipeline$CreateInfo;
import com.mojang.renderpearl.backend.api.BackendRenderPipeline$Pending;
import com.mojang.renderpearl.backend.api.GpuDeviceBackend;
import com.mojang.renderpearl.backend.api.GpuSurfaceBackend;
import com.mojang.renderpearl.backend.vulkan.checkpoints.CheckpointExtension;
import com.mojang.renderpearl.backend.vulkan.init.FeatureSet;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.vma.Vma;
import org.lwjgl.vulkan.EXTCalibratedTimestamps;
import org.lwjgl.vulkan.VK12;
import org.lwjgl.vulkan.VkBufferCopy;
import org.lwjgl.vulkan.VkCalibratedTimestampInfoEXT;
import org.lwjgl.vulkan.VkCalibratedTimestampInfoKHR;
import org.lwjgl.vulkan.VkDevice;
import org.lwjgl.vulkan.VkPhysicalDeviceLimits;
import org.lwjgl.vulkan.VkPhysicalDeviceVulkan11Properties;
import org.lwjgl.vulkan.VkBufferCopy.Buffer;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class VulkanDevice implements GpuDeviceBackend {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final VulkanInstance instance;
   private final VkDevice vkDevice;
   private final long vma;
   private final DeviceInfo deviceInfo;
   private final VulkanQueue graphicsQueue;
   private final VulkanQueue computeQueue;
   private final VulkanQueue transferQueue;
   private final boolean isIntegratedIntelMoltenVK;
   private final FeatureSet enabledFeatures;
   private final VulkanCommandEncoder commandEncoder;
   private final CheckpointExtension checkpointExtension;

   public VulkanDevice(
      final VulkanInstance instance,
      final VulkanPhysicalDevice physicalDevice,
      final FeatureSet enabledFeatureSet,
      final VkDevice vkDevice,
      final long vma,
      final CheckpointExtension checkpointExtension
   ) {
      this.instance = instance;
      this.vkDevice = vkDevice;
      this.vma = vma;
      this.enabledFeatures = enabledFeatureSet;
      this.checkpointExtension = checkpointExtension;
      Set<String> extensionNames = new HashSet<>();

      for (String name : instance.getEnabledExtensions()) {
         extensionNames.add(name + " (I)");
      }

      for (String name : enabledFeatureSet.extensions()) {
         extensionNames.add(name + " (D)");
      }

      VkPhysicalDeviceLimits limits = physicalDevice.vkPhysicalDeviceProperties().limits();
      VkPhysicalDeviceVulkan11Properties vk11Properties = physicalDevice.vkPhysicalDeviceVulkan11Properties();
      int indirectDrawCount = Integer.compareUnsigned(limits.maxDrawIndirectCount(), Integer.MAX_VALUE) > 0 ? Integer.MAX_VALUE : limits.maxDrawIndirectCount();
      this.deviceInfo = new DeviceInfo(
         physicalDevice.deviceName(),
         physicalDevice.vendorName(),
         physicalDevice.driverInfo(),
         true,
         "Vulkan",
         limits.timestampPeriod(),
         new DeviceLimits(
            (int)limits.maxSamplerAnisotropy(),
            (int)limits.minUniformBufferOffsetAlignment(),
            limits.maxImageDimension2D(),
            vk11Properties.maxMemoryAllocationSize() < 0L ? Long.MAX_VALUE : vk11Properties.maxMemoryAllocationSize(),
            physicalDevice.vkPhysicalDeviceMultiDrawPropertiesEXT().maxMultiDrawCount() < 0
               ? Integer.MAX_VALUE
               : physicalDevice.vkPhysicalDeviceMultiDrawPropertiesEXT().maxMultiDrawCount(),
            limits.maxColorAttachments(),
            indirectDrawCount
         ),
         new DeviceFeatures(
            enabledFeatureSet.contains(VulkanFeatureSets.WIREFRAME_FEATURESET),
            true,
            enabledFeatureSet.contains(VulkanFeatureSets.MULTI_DRAW_FEATURESET),
            false,
            true,
            true,
            true,
            true
         ),
         Collections.unmodifiableSet(extensionNames),
         new HintsAndWorkarounds(false, false, Util.isAppleSiliconMac(physicalDevice.deviceName()), false),
         physicalDevice.deviceType()
      );
      IntIntPair graphicsQueueFamily = physicalDevice.graphicsQueueFamilyAndIndex();

      assert graphicsQueueFamily != null;

      IntIntPair computeQueueFamily = physicalDevice.computeQueueFamilyAndIndex();
      IntIntPair transferQueueFamily = physicalDevice.transferQueueFamilyAndIndex();
      this.graphicsQueue = new VulkanQueue(this, graphicsQueueFamily.leftInt(), graphicsQueueFamily.rightInt());
      if (computeQueueFamily != null) {
         this.computeQueue = new VulkanQueue(this, computeQueueFamily.leftInt(), computeQueueFamily.rightInt());
      } else {
         this.computeQueue = this.graphicsQueue;
      }

      if (transferQueueFamily != null) {
         this.transferQueue = new VulkanQueue(this, transferQueueFamily.leftInt(), transferQueueFamily.rightInt());
      } else {
         this.transferQueue = this.computeQueue;
      }

      this.isIntegratedIntelMoltenVK = physicalDevice.vkPhysicalDeviceProperties().deviceType() == 1
         && physicalDevice.vkPhysicalDeviceProperties().vendorID() == 32902
         && physicalDevice.vkPhysicalDeviceDriverProperties().driverID() == 14;
      physicalDevice.close();
      this.commandEncoder = new VulkanCommandEncoder(this);
   }

   @Override
   public void close() {
      this.checkpointExtension.close();
      this.commandEncoder.destroy();
      Vma.vmaDestroyAllocator(this.vma);
      VK12.vkDestroyDevice(this.vkDevice, null);
      this.instance.close();
   }

   @Override
   public DeviceInfo getDeviceInfo() {
      return this.deviceInfo;
   }

   public VulkanInstance instance() {
      return this.instance;
   }

   public VkDevice vkDevice() {
      return this.vkDevice;
   }

   public VulkanQueue graphicsQueue() {
      return this.graphicsQueue;
   }

   public VulkanQueue computeQueue() {
      return this.computeQueue;
   }

   public VulkanQueue transferQueue() {
      return this.transferQueue;
   }

   public long vma() {
      return this.vma;
   }

   @Override
   public GpuSurfaceBackend createSurface(final long windowHandle, final BooleanSupplier isIconified) {
      return new VulkanGpuSurface(this, windowHandle);
   }

   public VulkanCommandEncoder createCommandEncoder() {
      return this.commandEncoder;
   }

   @Override
   public GpuSampler createSampler(
      final AddressMode addressModeU,
      final AddressMode addressModeV,
      final FilterMode minFilter,
      final FilterMode magFilter,
      final int maxAnisotropy,
      final OptionalDouble maxLod
   ) {
      return new VulkanGpuSampler(this, addressModeU, addressModeV, minFilter, magFilter, maxAnisotropy, maxLod);
   }

   @Override
   public GpuTexture createTexture(
      @Nullable final String label,
      @GpuTexture$Usage final int usage,
      final GpuFormat format,
      final int width,
      final int height,
      final int depthOrLayers,
      final int mipLevels
   ) {
      return new VulkanGpuTexture(this, usage, this.isDebuggingEnabled() && label != null ? label : "", format, width, height, depthOrLayers, mipLevels);
   }

   @Override
   public GpuTextureView createTextureView(final GpuTexture texture, final int baseMipLevel, final int mipLevels) {
      return new VulkanGpuTextureView(this, (VulkanGpuTexture)texture, baseMipLevel, mipLevels);
   }

   public VulkanGpuBuffer createBuffer(@Nullable final Supplier<String> label, @GpuBuffer$Usage final int usage, final long size) {
      return new VulkanGpuBuffer$Direct(this, label, usage, size, this.isIntegratedIntelMoltenVK);
   }

   @Override
   public GpuBuffer createBuffer(@Nullable final Supplier<String> label, @GpuBuffer$Usage final int usage, final ByteBuffer data) {
      VulkanGpuBuffer buffer = this.createBuffer(label, usage | 8, (long)data.remaining());
      GpuBufferSlice stagingBuffer = this.commandEncoder.transientMemory().uploadStaging(data, 1L, 16);
      MemoryStack stack = MemoryStack.stackPush();

      try {
         Buffer regions = VkBufferCopy.calloc(1, stack).srcOffset(stagingBuffer.offset()).dstOffset(0L).size(data.remaining());
         VK12.vkCmdCopyBuffer(this.commandEncoder.objectInitCommandBuffer(), ((VulkanGpuBuffer)stagingBuffer.buffer()).vkBuffer(), buffer.vkBuffer(), regions);
      } catch (Throwable var10) {
         if (stack != null) {
            try {
               stack.close();
            } catch (Throwable var9) {
               var10.addSuppressed(var9);
            }
         }

         throw var10;
      }

      if (stack != null) {
         stack.close();
      }

      return buffer;
   }

   @Override
   public List<String> getLastDebugMessages() {
      return List.of();
   }

   @Override
   public boolean isDebuggingEnabled() {
      return this.instance.debug().enabled();
   }

   @Override
   public BackendRenderPipeline$Pending compilePipeline(final BackendRenderPipeline$CreateInfo pipelineCreateInfo) {
      VulkanRenderPipeline pipeline = VulkanRenderPipeline.compile(this, pipelineCreateInfo);
      return () -> pipeline;
   }

   @Override
   public GpuQueryPool createTimestampQueryPool(final int size) {
      return new VulkanQueryPool(this, size);
   }

   @Override
   public long getTimestampCalibrationOffset() {
      double timestampPeriod = this.deviceInfo.timestampPeriod();
      if (!this.enabledFeatures.contains(VulkanFeatureSets.CALIBRATED_TIMESTAMP_FEATURESET)) {
         long deviceTime = this.commandEncoder.getTimestampNow();
         long hostTime = System.nanoTime();
         long deviceTimeInNanos = timestampPeriod == 1.0 ? deviceTime : (long)(deviceTime * timestampPeriod);
         return hostTime - deviceTimeInNanos;
      } else {
         MemoryStack stack = MemoryStack.stackPush();

         long var13;
         try {
            org.lwjgl.vulkan.VkCalibratedTimestampInfoEXT.Buffer infos = VkCalibratedTimestampInfoEXT.calloc(2, stack);
            ((VkCalibratedTimestampInfoKHR)infos.get(0)).sType$Default().timeDomain(0);
            ((VkCalibratedTimestampInfoKHR)infos.get(1)).sType$Default().timeDomain(1);
            LongBuffer timestampValues = stack.callocLong(infos.capacity());
            LongBuffer deviation = stack.callocLong(1);
            EXTCalibratedTimestamps.vkGetCalibratedTimestampsEXT(this.vkDevice, infos, timestampValues, deviation);
            long deviceTime = timestampValues.get(0);
            long hostTime = timestampValues.get(1);
            long deviceInNanos = timestampPeriod == 1.0 ? deviceTime : (long)(deviceTime * timestampPeriod);
            var13 = hostTime - deviceInNanos;
         } catch (Throwable var16) {
            if (stack != null) {
               try {
                  stack.close();
               } catch (Throwable var15) {
                  var16.addSuppressed(var15);
               }
            }

            throw var16;
         }

         if (stack != null) {
            stack.close();
         }

         return var13;
      }
   }

   public CheckpointExtension checkpointExtension() {
      return this.checkpointExtension;
   }
}
