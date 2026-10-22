package net.minecraft.world.level.levelgen.blending;

import com.google.common.primitives.Floats;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;

public record BlendingData$Packed(int minSection, int maxSection, Optional<float[]> heights) {
   private static final Codec<float[]> FLOAT_ARRAY_CODEC = Codec.FLOAT.listOf().xmap(Floats::toArray, Floats::asList);
   public static final Codec<BlendingData$Packed> CODEC = RecordCodecBuilder.create(
         i -> i.group(
               Codec.INT.fieldOf("min_section").forGetter(BlendingData$Packed::minSection),
               Codec.INT.fieldOf("max_section").forGetter(BlendingData$Packed::maxSection),
               FLOAT_ARRAY_CODEC.lenientOptionalFieldOf("heights").forGetter(BlendingData$Packed::heights)
            )
            .apply(i, BlendingData$Packed::new)
      )
      .validate(BlendingData$Packed::validateArraySize);

   private static DataResult<BlendingData$Packed> validateArraySize(final BlendingData$Packed blendingData) {
      return blendingData.heights.isPresent() && ((float[])blendingData.heights.get()).length != BlendingData.CELL_COLUMN_COUNT
         ? DataResult.error(() -> "heights has to be of length " + BlendingData.CELL_COLUMN_COUNT)
         : DataResult.success(blendingData);
   }
}
