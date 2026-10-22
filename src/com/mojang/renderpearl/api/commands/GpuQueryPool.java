package com.mojang.renderpearl.api.commands;

import com.mojang.renderpearl.util.UncheckedAutoCloseable;
import java.util.OptionalLong;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface GpuQueryPool extends UncheckedAutoCloseable {
   int size();

   OptionalLong getValue(int index);

   OptionalLong[] getValues(int index, int count);
}
