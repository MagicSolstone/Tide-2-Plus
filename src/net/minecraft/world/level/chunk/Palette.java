package net.minecraft.world.level.chunk;

import java.util.function.Predicate;
import net.minecraft.core.IdMap;
import net.minecraft.network.FriendlyByteBuf;

public interface Palette<T> {
   int idFor(T value, PaletteResize<T> resizeHandler);

   boolean maybeHas(Predicate<T> predicate);

   T valueFor(int index);

   void read(FriendlyByteBuf buffer, IdMap<T> globalMap);

   void write(FriendlyByteBuf buffer, IdMap<T> globalMap);

   int getSerializedSize(IdMap<T> globalMap);

   int getSize();

   Palette<T> copy();
}
