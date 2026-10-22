package com.mojang.renderpearl.api.textures;

import com.mojang.renderpearl.util.UncheckedAutoCloseable;
import java.util.OptionalDouble;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface GpuSampler extends UncheckedAutoCloseable {
   AddressMode getAddressModeU();

   AddressMode getAddressModeV();

   FilterMode getMinFilter();

   FilterMode getMagFilter();

   int getMaxAnisotropy();

   OptionalDouble getMaxLod();

   boolean isClosed();
}
