package net.minecraft.nbt;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.RecordBuilder.AbstractStringBuilder;
import java.util.Objects;
import java.util.Map.Entry;

class NbtOps$NbtRecordBuilder extends AbstractStringBuilder<Tag, CompoundTag> {
   protected NbtOps$NbtRecordBuilder(final NbtOps this$0) {
      Objects.requireNonNull(this$0);
      super(this$0);
   }

   protected CompoundTag initBuilder() {
      return new CompoundTag();
   }

   protected CompoundTag append(final String key, final Tag value, final CompoundTag builder) {
      builder.put(key, value);
      return builder;
   }

   protected DataResult<Tag> build(final CompoundTag builder, final Tag prefix) {
      if (prefix == null || prefix == EndTag.INSTANCE) {
         return DataResult.success(builder);
      } else if (!(prefix instanceof CompoundTag compound)) {
         return DataResult.error(() -> "mergeToMap called with not a map: " + prefix, prefix);
      } else {
         CompoundTag result = compound.shallowCopy();

         for (Entry<String, Tag> entry : builder.entrySet()) {
            result.put(entry.getKey(), entry.getValue());
         }

         return DataResult.success(result);
      }
   }
}
