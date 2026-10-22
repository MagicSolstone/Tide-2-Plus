package net.minecraft.world.level.storage.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;

public record ListOperation$StandAlone<T>(List<T> value, ListOperation operation) {
   public static <T> Codec<ListOperation$StandAlone<T>> codec(final Codec<T> valueCodec, final int maxSize) {
      return RecordCodecBuilder.create(
         i -> i.group(valueCodec.sizeLimitedListOf(maxSize).fieldOf("values").forGetter(f -> f.value), ListOperation.codec(maxSize).forGetter(f -> f.operation))
            .apply(i, ListOperation$StandAlone::new)
      );
   }

   public List<T> apply(final List<T> input) {
      return this.operation.apply(input, this.value);
   }
}
