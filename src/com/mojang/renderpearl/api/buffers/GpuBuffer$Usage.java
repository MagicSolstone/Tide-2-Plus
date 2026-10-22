package com.mojang.renderpearl.api.buffers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE_USE})
@Environment(EnvType.CLIENT)
public @interface GpuBuffer$Usage {
}
