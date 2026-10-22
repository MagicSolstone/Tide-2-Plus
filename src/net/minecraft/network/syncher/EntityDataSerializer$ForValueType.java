package net.minecraft.network.syncher;

public interface EntityDataSerializer$ForValueType<T> extends EntityDataSerializer<T> {
   @Override
   default T copy(final T value) {
      return value;
   }
}
