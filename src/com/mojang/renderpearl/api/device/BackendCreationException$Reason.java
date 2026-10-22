package com.mojang.renderpearl.api.device;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum BackendCreationException$Reason {
   PLATFORM_ERROR("platform_error"),
   VULKAN_LOADER_MISSING("vulkan_loader_missing"),
   VULKAN_INSTANCE_CREATION_FAILED("vulkan_instance_creation_failed"),
   VULKAN_NO_DEVICE("vulkan_no_device"),
   VULKAN_KNOWN_PROBLEMATIC("vulkan_known_problematic"),
   VULKAN_DEVICE_VERSION_TOO_LOW("vulkan_device_version_too_low"),
   VULKAN_NO_GRAPHICS_QUEUE("vulkan_no_graphics_queue"),
   VULKAN_MISSING_EXTENSION("vulkan_missing_extension"),
   VULKAN_MISSING_FEATURE("vulkan_missing_feature"),
   OPENGL_MISSING("opengl_missing"),
   OTHER("other");

   private final String displayName;

   private BackendCreationException$Reason(final String key) {
      this.displayName = key;
   }

   public String displayName() {
      return this.displayName;
   }
}
