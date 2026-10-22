package net.minecraft.world.level.storage;

import com.google.common.collect.Streams;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Stream;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.ProblemReporter$IndexedPathElement;

class TagValueInput$CompoundListWrapper implements ValueInput$ValueInputList {
   private final ProblemReporter problemReporter;
   private final ValueInputContextHelper context;
   private final List<CompoundTag> list;

   public TagValueInput$CompoundListWrapper(final ProblemReporter problemReporter, final ValueInputContextHelper context, final List<CompoundTag> list) {
      this.problemReporter = problemReporter;
      this.context = context;
      this.list = list;
   }

   private ValueInput wrapChild(final int index, final CompoundTag compoundTag) {
      return TagValueInput.wrapChild(this.problemReporter.forChild(new ProblemReporter$IndexedPathElement(index)), this.context, compoundTag);
   }

   @Override
   public boolean isEmpty() {
      return this.list.isEmpty();
   }

   @Override
   public Stream<ValueInput> stream() {
      return Streams.mapWithIndex(this.list.stream(), (value, index) -> this.wrapChild((int)index, value));
   }

   @Override
   public Iterator<ValueInput> iterator() {
      ListIterator<CompoundTag> iterator = this.list.listIterator();
      return new TagValueInput$CompoundListWrapper$1(this, iterator);
   }
}
