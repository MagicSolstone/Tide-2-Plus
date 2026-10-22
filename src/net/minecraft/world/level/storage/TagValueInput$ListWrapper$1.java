package net.minecraft.world.level.storage;

import com.google.common.collect.AbstractIterator;
import java.util.Iterator;
import java.util.Objects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jspecify.annotations.Nullable;

class TagValueInput$ListWrapper$1 extends AbstractIterator<ValueInput> {
   private int index;

   TagValueInput$ListWrapper$1(final TagValueInput$ListWrapper this$0, final Iterator val$iterator) {
      this.val$iterator = val$iteratorx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Nullable
   protected ValueInput computeNext() {
      while (this.val$iterator.hasNext()) {
         Tag value = (Tag)this.val$iterator.next();
         int currentIndex = this.index++;
         if (value instanceof CompoundTag compoundTag) {
            return TagValueInput.wrapChild(this.this$0.reporterForChild(currentIndex), this.this$0.context, compoundTag);
         }

         this.this$0.reportIndexUnwrapProblem(currentIndex, value);
      }

      return (ValueInput)this.endOfData();
   }
}
