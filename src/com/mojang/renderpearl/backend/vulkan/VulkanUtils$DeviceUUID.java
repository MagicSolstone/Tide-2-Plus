package com.mojang.renderpearl.backend.vulkan;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record VulkanUtils$DeviceUUID(int driverID, int vendorID, int deviceID) {
}
