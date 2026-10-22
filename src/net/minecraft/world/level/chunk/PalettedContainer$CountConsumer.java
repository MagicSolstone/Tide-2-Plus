package net.minecraft.world.level.chunk;

@FunctionalInterface
public interface PalettedContainer$CountConsumer<T> {
   void accept(final T entry, final int count);
}
