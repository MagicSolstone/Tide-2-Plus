package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.util.ExtraCodecs;
import org.slf4j.Logger;

public record ListOperation$Insert(int offset) implements ListOperation {
   private static final Logger LOGGER = LogUtils.getLogger();
   public static final MapCodec<ListOperation$Insert> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("offset", 0).forGetter(ListOperation$Insert::offset)).apply(i, ListOperation$Insert::new)
   );

   @Override
   public ListOperation$Type mode() {
      return ListOperation$Type.INSERT;
   }

   @Override
   public <T> List<T> apply(final List<T> original, final List<T> replacement, final int maxSize) {
      int originalSize = original.size();
      if (this.offset > originalSize) {
         LOGGER.error("Cannot insert when offset is out of bounds");
         return original;
      } else if (originalSize + replacement.size() > maxSize) {
         LOGGER.error("Contents overflow in section insertion");
         return original;
      } else {
         Builder<T> newList = ImmutableList.builder();
         newList.addAll(original.subList(0, this.offset));
         newList.addAll(replacement);
         newList.addAll(original.subList(this.offset, originalSize));
         return newList.build();
      }
   }
}
