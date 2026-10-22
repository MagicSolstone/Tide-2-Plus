package com.mojang.blaze3d.systems;

import com.mojang.renderpearl.api.commands.GpuFence;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record RenderSystem$GpuAsyncTask(Runnable callback, GpuFence fence) {
}
