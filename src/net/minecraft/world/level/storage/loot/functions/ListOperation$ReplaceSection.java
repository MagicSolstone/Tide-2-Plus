package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import net.minecraft.util.ExtraCodecs;
import org.slf4j.Logger;

public record ListOperation$ReplaceSection(int offset, Optional<Integer> size) implements ListOperation {
   private static final Logger LOGGER = LogUtils.getLogger();
   public static final MapCodec<ListOperation$ReplaceSection> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("offset", 0).forGetter(ListOperation$ReplaceSection::offset),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("size").forGetter(ListOperation$ReplaceSection::size)
         )
         .apply(i, ListOperation$ReplaceSection::new)
   );

   public ListOperation$ReplaceSection(final int offset) {
      this(offset, Optional.empty());
   }

   @Override
   public ListOperation$Type mode() {
      return ListOperation$Type.REPLACE_SECTION;
   }

   @Override
   public <T> List<T> apply(final List<T> original, final List<T> replacement, final int maxSize) {
      int originalSize = original.size();
      if (this.offset > originalSize) {
         LOGGER.error("Cannot replace when offset is out of bounds");
         return original;
      } else {
         Builder<T> newList = ImmutableList.builder();
         newList.addAll(original.subList(0, this.offset));
         newList.addAll(replacement);
         int resumeIndex = this.offset + this.size.orElse(replacement.size());
         if (resumeIndex < originalSize) {
            newList.addAll(original.subList(resumeIndex, originalSize));
         }

         List<T> result = newList.build();
         if (result.size() > maxSize) {
            LOGGER.error("Contents overflow in section replacement");
            return original;
         } else {
            return result;
         }
      }
   }
}
