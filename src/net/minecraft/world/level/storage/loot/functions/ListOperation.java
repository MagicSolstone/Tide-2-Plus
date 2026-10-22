package net.minecraft.world.level.storage.loot.functions;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import java.util.List;

public interface ListOperation {
   MapCodec<ListOperation> UNLIMITED_CODEC = codec(Integer.MAX_VALUE);

   static MapCodec<ListOperation> codec(final int maxSize) {
      return ListOperation$Type.CODEC.dispatchMap("mode", ListOperation::mode, e -> e.mapCodec).validate(op -> {
         if (op instanceof ListOperation$ReplaceSection section && section.size().isPresent()) {
            int size = section.size().get();
            if (size > maxSize) {
               return DataResult.error(() -> "Size value too large: " + size + ", max size is " + maxSize);
            }
         }

         return DataResult.success(op);
      });
   }

   ListOperation$Type mode();

   default <T> List<T> apply(final List<T> original, final List<T> replacement) {
      return this.apply(original, replacement, Integer.MAX_VALUE);
   }

   <T> List<T> apply(List<T> original, List<T> replacement, int maxSize);
}
