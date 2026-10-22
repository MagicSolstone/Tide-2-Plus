package net.minecraft.world.level.storage;

import java.util.stream.Stream;

public interface ValueInput$TypedInputList<T> extends Iterable<T> {
   boolean isEmpty();

   Stream<T> stream();
}
