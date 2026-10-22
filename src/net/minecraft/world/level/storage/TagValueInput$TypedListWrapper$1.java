package net.minecraft.world.level.storage;

import com.google.common.collect.AbstractIterator;
import com.mojang.serialization.DataResult.Error;
import com.mojang.serialization.DataResult.Success;
import java.util.ListIterator;
import java.util.Objects;
import net.minecraft.nbt.Tag;
import org.jspecify.annotations.Nullable;

class TagValueInput$TypedListWrapper$1 extends AbstractIterator<T> {
   TagValueInput$TypedListWrapper$1(final TagValueInput$TypedListWrapper this$0, final ListIterator val$iterator) {
      this.val$iterator = val$iteratorx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Nullable
   protected T computeNext() {
      while (this.val$iterator.hasNext()) {
         int index = this.val$iterator.nextIndex();
         Tag value = (Tag)this.val$iterator.next();
         switch (this.this$0.codec.parse(this.this$0.context.ops(), value)) {
            case Success<T> success:
               return (T)success.value();
            case Error<T> error:
               this.this$0.reportIndexUnwrapProblem(index, value, error);
               if (!error.partialValue().isPresent()) {
                  break;
               }

               return (T)error.partialValue().get();
            default:
               throw new MatchException(null, null);
         }
      }

      return (T)this.endOfData();
   }
}
