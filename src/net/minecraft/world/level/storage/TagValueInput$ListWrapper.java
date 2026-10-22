package net.minecraft.world.level.storage;

import com.google.common.collect.Streams;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.ProblemReporter$IndexedFieldPathElement;

class TagValueInput$ListWrapper implements ValueInput$ValueInputList {
   private final ProblemReporter problemReporter;
   private final String name;
   private final ValueInputContextHelper context;
   private final ListTag list;

   private TagValueInput$ListWrapper(final ProblemReporter problemReporter, final String name, final ValueInputContextHelper context, final ListTag list) {
      this.problemReporter = problemReporter;
      this.name = name;
      this.context = context;
      this.list = list;
   }

   @Override
   public boolean isEmpty() {
      return this.list.isEmpty();
   }

   private ProblemReporter reporterForChild(final int index) {
      return this.problemReporter.forChild(new ProblemReporter$IndexedFieldPathElement(this.name, index));
   }

   private void reportIndexUnwrapProblem(final int index, final Tag value) {
      this.problemReporter.report(new TagValueInput$UnexpectedListElementTypeProblem(this.name, index, CompoundTag.TYPE, value.getType()));
   }

   @Override
   public Stream<ValueInput> stream() {
      return Streams.mapWithIndex(this.list.stream(), (value, index) -> {
         if (value instanceof CompoundTag compoundTag) {
            return TagValueInput.wrapChild(this.reporterForChild((int)index), this.context, compoundTag);
         } else {
            this.reportIndexUnwrapProblem((int)index, value);
            return null;
         }
      }).filter(Objects::nonNull);
   }

   @Override
   public Iterator<ValueInput> iterator() {
      Iterator<Tag> iterator = this.list.iterator();
      return new TagValueInput$ListWrapper$1(this, iterator);
   }
}
