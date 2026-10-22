package net.minecraft.world.level.storage;

import com.google.common.collect.AbstractIterator;
import java.util.ListIterator;
import java.util.Objects;
import net.minecraft.nbt.CompoundTag;
import org.jspecify.annotations.Nullable;

class TagValueInput$CompoundListWrapper$1 extends AbstractIterator<ValueInput> {
   TagValueInput$CompoundListWrapper$1(final TagValueInput$CompoundListWrapper this$0, final ListIterator val$iterator) {
      this.val$iterator = val$iteratorx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Nullable
   protected ValueInput computeNext() {
      if (this.val$iterator.hasNext()) {
         int index = this.val$iterator.nextIndex();
         CompoundTag value = (CompoundTag)this.val$iterator.next();
         return this.this$0.wrapChild(index, value);
      } else {
         return (ValueInput)this.endOfData();
      }
   }
}
