package net.minecraft.world.level.storage;

import com.google.common.collect.Streams;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult.Error;
import com.mojang.serialization.DataResult.Success;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Objects;
import java.util.stream.Stream;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.ProblemReporter;

class TagValueInput$TypedListWrapper<T> implements ValueInput$TypedInputList<T> {
   private final ProblemReporter problemReporter;
   private final String name;
   private final ValueInputContextHelper context;
   private final Codec<T> codec;
   private final ListTag list;

   private TagValueInput$TypedListWrapper(
      final ProblemReporter problemReporter, final String name, final ValueInputContextHelper context, final Codec<T> codec, final ListTag list
   ) {
      this.problemReporter = problemReporter;
      this.name = name;
      this.context = context;
      this.codec = codec;
      this.list = list;
   }

   @Override
   public boolean isEmpty() {
      return this.list.isEmpty();
   }

   private void reportIndexUnwrapProblem(final int index, final Tag value, final Error<?> error) {
      this.problemReporter.report(new TagValueInput$DecodeFromListFailedProblem(this.name, index, value, error));
   }

   @Override
   public Stream<T> stream() {
      return Streams.mapWithIndex(this.list.stream(), (value, index) -> {
         return switch (this.codec.parse(this.context.ops(), value)) {
            case Success<T> success -> success.value();
            case Error<T> error -> {
               this.reportIndexUnwrapProblem((int)index, value, error);
               yield error.partialValue().orElse(null);
            }
            default -> throw new MatchException(null, null);
         };
      }).filter(Objects::nonNull);
   }

   @Override
   public Iterator<T> iterator() {
      ListIterator<Tag> iterator = this.list.listIterator();
      return new TagValueInput$TypedListWrapper$1(this, iterator);
   }
}
