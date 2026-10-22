package net.minecraft.world.level.chunk;

import com.mojang.serialization.DataResult;

public interface PalettedContainerRO$Unpacker<T, C extends PalettedContainerRO<T>> {
   DataResult<C> read(Strategy<T> strategy, PalettedContainerRO$PackedData<T> discData);
}
