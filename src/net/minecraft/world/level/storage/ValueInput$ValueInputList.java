package net.minecraft.world.level.storage;

import java.util.stream.Stream;

public interface ValueInput$ValueInputList extends Iterable<ValueInput> {
   boolean isEmpty();

   Stream<ValueInput> stream();
}
