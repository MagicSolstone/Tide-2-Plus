package net.minecraft.world.level.chunk;

import com.google.common.annotations.VisibleForTesting;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.network.FriendlyByteBuf;

public interface PalettedContainerRO<T> {
   T get(int x, int y, int z);

   void getAll(Consumer<T> consumer);

   void write(FriendlyByteBuf buffer);

   int getSerializedSize();

   @VisibleForTesting
   int bitsPerEntry();

   boolean maybeHas(Predicate<T> predicate);

   void forEachInPalette(Consumer<T> consumer);

   void count(PalettedContainer$CountConsumer<T> output);

   PalettedContainer<T> copy();

   PalettedContainer<T> recreate();

   PalettedContainerRO$PackedData<T> pack(Strategy<T> strategy);
}
